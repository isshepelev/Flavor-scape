package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Notification;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.service.dto.NotificationDto;

import java.util.List;

public interface NotificationService {


    Notification createNotification(String title, String message, User user);

    List<NotificationDto> getUserNotifications(String username);

    List<NotificationDto> getUserNotificationsNotRead(String username);

    void markAsRead(Long notificationId);

    NotificationDto getNotificationById(Long notificationId);

}
