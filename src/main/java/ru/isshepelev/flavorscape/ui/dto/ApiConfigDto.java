package ru.isshepelev.flavorscape.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiConfigDto {
    private double latitude;
    private double longitude;
    String radius;
    String placeName;
    boolean workTime;
}
