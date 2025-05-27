package ru.isshepelev.flavorscape.infrastructure.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestEventDto {
    private Long senderId;
    private String senderUsername;
    private Long recipientId;
    private Long requestId;
}