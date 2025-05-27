package ru.isshepelev.flavorscape.infrastructure.service.dto;

import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        String title,
        String message,
        LocalDateTime createdAt,
        boolean isRead
) {}
