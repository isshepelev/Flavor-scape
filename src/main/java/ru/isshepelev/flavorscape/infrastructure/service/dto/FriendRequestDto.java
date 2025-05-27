package ru.isshepelev.flavorscape.infrastructure.service.dto;

import java.time.LocalDateTime;

public record FriendRequestDto(
        Long requestId,
        Long senderId,
        String senderUsername,
        LocalDateTime createdAt
) {}
