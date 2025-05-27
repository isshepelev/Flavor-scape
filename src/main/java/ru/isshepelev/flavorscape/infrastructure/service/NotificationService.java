package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Notification;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;

import java.util.List;

public interface NotificationService {


    Notification createNotification(String title, String message, User user);

    List<Notification> getUserNotifications(Long userId);

    void markAsRead(Long notificationId);
}
