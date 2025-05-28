package ru.isshepelev.flavorscape.infrastructure.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.exception.AlreadyFriendsException;
import ru.isshepelev.flavorscape.infrastructure.exception.FriendRequestAlreadySentException;
import ru.isshepelev.flavorscape.infrastructure.exception.UserBlockedException;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.UserFriend;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.enums.FriendStatus;
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

import static ru.isshepelev.flavorscape.infrastructure.persistance.entity.enums.FriendStatus.*;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final NotificationService notificationService;
    private final KafkaTemplate<String, FriendRequestEventDto> kafkaTemplate;

    private static final String FRIEND_REQUEST_TOPIC = "friend-requests";

    @Override
    public void sendFriendRequest(String senderUsername, Long recipientId) {
        User sender = userRepository.findByUsername(senderUsername);
        if (sender == null) throw new EntityNotFoundException("User not found with username: " + senderUsername);
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + recipientId));

        Optional<UserFriend> existingRelation = userFriendRepository.findByUserAndFriend(sender, recipient);

        if (existingRelation.isPresent()) {
            FriendStatus currentStatus = existingRelation.get().getStatus();

            switch (currentStatus) {
                case PENDING:
                    throw new FriendRequestAlreadySentException("Friend request already sent and is pending");
                case ACCEPTED:
                    throw new AlreadyFriendsException("You are already friends with this user");
                case REJECTED:
                    existingRelation.get().setStatus(PENDING);
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
        friendRequest.setStatus(PENDING);
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
    public void acceptFriendRequest(Long requestId, String username) {
        UserFriend friendRequest = userFriendRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException("Friend request not found"));

        if (!friendRequest.getFriend().getId().equals(userRepository.findByUsername(username).getId())) {
            throw new SecurityException("You can't accept this friend request");
        }

        if (friendRequest.getStatus() == ACCEPTED) {
            throw new AlreadyFriendsException("Friend request already accepted");
        }

        if (friendRequest.getStatus() != PENDING) {
            throw new IllegalStateException("Friend request is not in pending status");
        }

        boolean alreadyFriends = userFriendRepository.existsByUserAndFriendAndStatus(
                friendRequest.getFriend(),
                friendRequest.getUser(),
                ACCEPTED
        );

        if (alreadyFriends) {
            throw new AlreadyFriendsException("You are already friends with this user");
        }

        friendRequest.setStatus(ACCEPTED);
        userFriendRepository.save(friendRequest);

        if (!userFriendRepository.existsByUserAndFriend(friendRequest.getFriend(), friendRequest.getUser())) {
            UserFriend reciprocalFriend = new UserFriend();
            reciprocalFriend.setUser(friendRequest.getFriend());
            reciprocalFriend.setFriend(friendRequest.getUser());
            reciprocalFriend.setStatus(ACCEPTED);
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
    public void rejectFriendRequest(Long requestId, String username) {
        UserFriend friendRequest = userFriendRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Friend request not found"));

        if (!friendRequest.getFriend().getId().equals(userRepository.findByUsername(username).getId())) {
            throw new SecurityException("You can't reject this friend request");
        }

        if (friendRequest.getStatus() == ACCEPTED) {
            throw new IllegalStateException("Cannot reject already accepted friend request");
        }

        friendRequest.setStatus(REJECTED);
        userFriendRepository.save(friendRequest);

        notificationService.createNotification(
                "Запрос на добавление в друзья отклонен",
                friendRequest.getFriend().getUsername() + " отклонил ваш запрос на добавление в друзья",
                friendRequest.getUser()
        );
    }

    @Override
    @Transactional
    public void blockedUserRequest(Long requestId, String username){
        User currentUser = userRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException("Current user not found"));
        User userToBlock = userRepository.findByUsername(username);
        if (userToBlock == null) throw new EntityNotFoundException("User to block not found");

        userFriendRepository.deleteByUserAndFriend(currentUser,userToBlock);
        userFriendRepository.deleteByUserAndFriend(userToBlock,currentUser);

        UserFriend blockRelation = new UserFriend();
        blockRelation.setUser(currentUser);
        blockRelation.setFriend(userToBlock);
        blockRelation.setStatus(FriendStatus.BLOCKED);
        blockRelation.setCreatedAt(LocalDateTime.now());
        userFriendRepository.save(blockRelation);
    }

    @Override
    @Transactional
    public void removeFromFriendsRequest(Long requestId, String username){
        User currentUser = userRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException("Current user not found"));
        User friendToRemove  = userRepository.findByUsername(username);
        if (friendToRemove == null) throw new EntityNotFoundException("User to remove not found");

        boolean isFriend = userFriendRepository.existsByUserAndFriendAndStatus(currentUser,friendToRemove, ACCEPTED);

        if (!isFriend) throw new IllegalStateException("This user is not in your friends list");


        userFriendRepository.deleteByUserAndFriend(currentUser, friendToRemove);
        userFriendRepository.deleteByUserAndFriend(friendToRemove, currentUser);

    }

    @Override
    public List<FriendRequestDto> getPendingRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userFriendRepository.findByFriendAndStatus(user, PENDING)
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

        return userFriendRepository.findByUserAndStatus(user, ACCEPTED)
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
