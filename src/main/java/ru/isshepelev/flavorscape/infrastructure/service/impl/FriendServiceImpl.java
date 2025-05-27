package ru.isshepelev.flavorscape.infrastructure.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.exception.AlreadyFriendsException;
import ru.isshepelev.flavorscape.infrastructure.exception.FriendRequestAlreadySentException;
import ru.isshepelev.flavorscape.infrastructure.exception.UserBlockedException;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.UserFriend;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserFriendRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserRepository;
import ru.isshepelev.flavorscape.infrastructure.service.FriendService;
import ru.isshepelev.flavorscape.infrastructure.service.NotificationService;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendDto;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendRequestDto;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendRequestEventDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final NotificationService notificationService;
    private final KafkaTemplate<String, FriendRequestEventDto> kafkaTemplate;

    private static final String FRIEND_REQUEST_TOPIC = "friend-requests";

    @Override
    public void sendFriendRequest(Long senderId, Long recipientId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + senderId));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + recipientId));

        Optional<UserFriend> existingRelation = userFriendRepository.findByUserAndFriend(sender, recipient);

        if (existingRelation.isPresent()) {
            UserFriend.FriendStatus currentStatus = existingRelation.get().getStatus();

            switch (currentStatus) {
                case PENDING:
                    throw new FriendRequestAlreadySentException("Friend request already sent and is pending");
                case ACCEPTED:
                    throw new AlreadyFriendsException("You are already friends with this user");
                case REJECTED:
                    existingRelation.get().setStatus(UserFriend.FriendStatus.PENDING);
                    existingRelation.get().setCreatedAt(LocalDateTime.now());
                    userFriendRepository.save(existingRelation.get());

                    sendFriendRequestEvent(sender, recipient, existingRelation.get().getId());
                    return;
                case BLOCKED:
                    throw new UserBlockedException("You cannot send friend request to this user");
            }
        }

        UserFriend friendRequest = new UserFriend();
        friendRequest.setUser(sender);
        friendRequest.setFriend(recipient);
        friendRequest.setStatus(UserFriend.FriendStatus.PENDING);
        friendRequest.setCreatedAt(LocalDateTime.now());
        userFriendRepository.save(friendRequest);

        sendFriendRequestEvent(sender, recipient, friendRequest.getId());

        notificationService.createNotification(
                "Запрос в друзья",
                sender.getUsername() + " хочет добавить вас в друзья!",
                recipient
        );
    }

    @Override
    public void acceptFriendRequest(Long requestId, Long userId) {
        UserFriend friendRequest = userFriendRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException("Friend request not found"));

        if (!friendRequest.getFriend().getId().equals(userId)) {
            throw new SecurityException("You can't accept this friend request");
        }

        if (friendRequest.getStatus() == UserFriend.FriendStatus.ACCEPTED) {
            throw new AlreadyFriendsException("Friend request already accepted");
        }

        boolean alreadyFriends = userFriendRepository.existsByUserAndFriendAndStatus(
                friendRequest.getFriend(),
                friendRequest.getUser(),
                UserFriend.FriendStatus.ACCEPTED
        );

        if (alreadyFriends) {
            throw new AlreadyFriendsException("You are already friends with this user");
        }

        friendRequest.setStatus(UserFriend.FriendStatus.ACCEPTED);
        userFriendRepository.save(friendRequest);

        if (!userFriendRepository.existsByUserAndFriend(friendRequest.getFriend(), friendRequest.getUser())) {
            UserFriend reciprocalFriend = new UserFriend();
            reciprocalFriend.setUser(friendRequest.getFriend());
            reciprocalFriend.setFriend(friendRequest.getUser());
            reciprocalFriend.setStatus(UserFriend.FriendStatus.ACCEPTED);
            reciprocalFriend.setCreatedAt(LocalDateTime.now());
            userFriendRepository.save(reciprocalFriend);
        }

        notificationService.createNotification(
                "Запрос на добавление в друзья принят",
                friendRequest.getFriend().getUsername() + " принял ваш запрос на добавление в друзья",
                friendRequest.getUser()
        );
    }

    @Override
    public void rejectFriendRequest(Long requestId, Long userId) {
        UserFriend friendRequest = userFriendRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Friend request not found"));

        if (!friendRequest.getFriend().getId().equals(userId)) {
            throw new SecurityException("You can't reject this friend request");
        }

        friendRequest.setStatus(UserFriend.FriendStatus.REJECTED);
        userFriendRepository.save(friendRequest);

        notificationService.createNotification(
                "Запрос на добавление в друзья отклонен",
                friendRequest.getFriend().getUsername() + " отклонил ваш запрос на добавление в друзья",
                friendRequest.getUser()
        );
    }

    @Override
    public List<FriendRequestDto> getPendingRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userFriendRepository.findByFriendAndStatus(user, UserFriend.FriendStatus.PENDING)
                .stream()
                .map(request -> new FriendRequestDto(
                        request.getId(),
                        request.getUser().getId(),
                        request.getUser().getUsername(),
                        request.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendDto> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userFriendRepository.findByUserAndStatus(user, UserFriend.FriendStatus.ACCEPTED)
                .stream()
                .map(UserFriend::getFriend)
                .map(friend -> new FriendDto(
                        friend.getId(),
                        friend.getUsername()
                ))
                .collect(Collectors.toList());
    }

    private void sendFriendRequestEvent(User sender, User recipient, Long requestId) {
        FriendRequestEventDto event = new FriendRequestEventDto(
                sender.getId(),
                sender.getUsername(),
                recipient.getId(),
                requestId
        );
        kafkaTemplate.send(FRIEND_REQUEST_TOPIC, event);
    }
}
