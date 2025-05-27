package ru.isshepelev.flavorscape.ui.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Notification;
import ru.isshepelev.flavorscape.infrastructure.service.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public ResponseEntity<?> getNotification(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(notificationService.getUserNotifications(userDetails.getUsername()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
    }

    @GetMapping("/read-false")
    public ResponseEntity<?> getNotificationReadIsFalse(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(notificationService.getUserNotificationsNotRead(userDetails.getUsername()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long notificationId){
        return ResponseEntity.ok(notificationService.getNotificationById(notificationId));
    }

    @PostMapping("/{notificationId}")
    public void markRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }
}
