package ru.isshepelev.flavorscape.infrastructure.service.dto;

import jakarta.persistence.*;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Critique;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Place;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.enums.GeneralImpression;

import java.time.LocalDateTime;

public record ReviewRequestDto(

        Long id,
        LocalDateTime createdAt,
        GeneralImpression generalImpression,
        Critique critique,
        double generalRating,
        String content,
        String authorUsername

) {

}
