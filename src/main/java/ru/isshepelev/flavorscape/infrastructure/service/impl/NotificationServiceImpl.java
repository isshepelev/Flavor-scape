package ru.isshepelev.flavorscape.infrastructure.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Notification;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.NotificationRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserRepository;
import ru.isshepelev.flavorscape.infrastructure.service.FriendService;
import ru.isshepelev.flavorscape.infrastructure.service.NotificationService;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendDto;
import ru.isshepelev.flavorscape.infrastructure.service.dto.NotificationDto;
import ru.isshepelev.flavorscape.ui.dto.NotificationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;

    @Override
    public void createNotification(String title, String message, User user) {
        Notification notification = new Notification(title, message, user);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDto> getUserNotifications(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream().map(this::convertToDto).toList();
    }

    @Override
    public List<NotificationDto> getUserNotificationsNotRead(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        return notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user).stream().map(this::convertToDto).toList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }

    @Override
    public NotificationDto getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId).stream().map(e ->
                new NotificationDto(
                        e.getId(),
                        e.getTitle(),
                        e.getMessage(),
                        e.getCreatedAt(),
                        e.isRead()
                )).findAny().orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));
    }
    @Override
    public void notificateFriends(String username, NotificationRequestDto notificationRequestDto) {
        User user = userRepository.findByUsername(username);
        List<Long> userFriendsIds = friendService.getFriends(user.getId()).stream().map(FriendDto::friendId).toList();

        notificationRequestDto.getFriendsId().stream().filter(userFriendsIds::contains).forEach(friendId ->{
                    User friend = userRepository.findById(friendId).orElseThrow(() -> new EntityNotFoundException("user not found"));
                    createNotification(notificationRequestDto.getTitle(), notificationRequestDto.getMessage(), friend);
                }

        );

    }

    private NotificationDto convertToDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getCreatedAt(),
                notification.isRead());
    }
}
