package ru.isshepelev.flavorscape.ui.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.flavorscape.infrastructure.service.NotificationService;
import ru.isshepelev.flavorscape.ui.dto.NotificationRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public ResponseEntity<?> getNotifications(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false, defaultValue = "false") boolean unreadOnly) {

        try {
            if (unreadOnly) {
                return ResponseEntity.ok(notificationService.getUserNotificationsNotRead(userDetails.getUsername()));
            } else {
                return ResponseEntity.ok(notificationService.getUserNotifications(userDetails.getUsername()));
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }

    }

    @PostMapping("/friends")
    public ResponseEntity<?> notificationFriends(@AuthenticationPrincipal UserDetails userDetails, @RequestBody NotificationRequestDto notificationRequestDto){
        notificationService.notificateFriends(userDetails.getUsername(), notificationRequestDto);
        return ResponseEntity.ok().build();
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
