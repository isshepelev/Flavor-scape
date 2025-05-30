package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Place;
import ru.isshepelev.flavorscape.infrastructure.service.dto.OrganizationDto;
import ru.isshepelev.flavorscape.ui.dto.PlaceRequestDto;
import ru.isshepelev.flavorscape.ui.dto.PlaceReviewDto;

import java.util.List;

public interface PlaceService {

    void savePlacesToDatabase(List<OrganizationDto> organizationDto) throws InterruptedException;

    PlaceRequestDto getPlaceById(Long placeId, String username);

}
