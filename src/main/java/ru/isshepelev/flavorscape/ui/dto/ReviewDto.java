package ru.isshepelev.flavorscape.ui.dto;

import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Critique;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.enums.GeneralImpression;


public record ReviewDto(
        GeneralImpression generalImpression,
        Critique critique,
        int generalRating,
        String content
) {
}
