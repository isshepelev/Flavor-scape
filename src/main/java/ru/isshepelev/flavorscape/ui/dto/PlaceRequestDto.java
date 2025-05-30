package ru.isshepelev.flavorscape.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class PlaceRequestDto {
    private Long id;

    private String name;
    private String addressName;
    private String addressComment;

    private List<PlaceReviewDto> placeReviewDto;

}
