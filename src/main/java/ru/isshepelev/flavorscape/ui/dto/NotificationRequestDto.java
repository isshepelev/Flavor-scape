package ru.isshepelev.flavorscape.ui.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationRequestDto{
    private String title;
    private String message;
    private List<Long> friendsId;
}
