package ru.isshepelev.flavorscape.ui.dto;

import lombok.Data;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Critique;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.enums.GeneralImpression;

import java.util.Date;


@Data
public class PlaceReviewDto {
    private Long id;
    private GeneralImpression generalImpression;
    private Critique critique;
    private double generalRating;
    private String content;

    private Long authorId;
    private String authorName;
}
