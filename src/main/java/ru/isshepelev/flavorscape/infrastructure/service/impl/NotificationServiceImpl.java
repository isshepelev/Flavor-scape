package ru.isshepelev.flavorscape.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Notification;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.NotificationRepository;
import ru.isshepelev.flavorscape.infrastructure.service.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(String title, String message, User user) {
        Notification notification = new Notification(title,message,user);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc (userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> notification.setRead(true));
    }
}
