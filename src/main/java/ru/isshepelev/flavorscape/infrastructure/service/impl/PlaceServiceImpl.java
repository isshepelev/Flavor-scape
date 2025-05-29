package ru.isshepelev.flavorscape.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Place;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.PlaceRepository;
import ru.isshepelev.flavorscape.infrastructure.service.PlaceService;
import ru.isshepelev.flavorscape.infrastructure.service.dto.OrganizationDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    @Override
    public void savePlacesToDatabase(List<OrganizationDto> organizationDtos){
        if (organizationDtos == null) {
            throw new IllegalArgumentException("Organization DTO list cannot be null");
        }

        if (organizationDtos.isEmpty()) {
            return;
        }

        List<Place> places = organizationDtos.stream().filter(Objects::nonNull).map(this::convertToEntity).toList();

        placeRepository.saveAll(places);

    }

    private Place convertToEntity(OrganizationDto dto) {
        Place place = new Place();
        place.setId(dto.id());
        place.setName(dto.name());
        place.setAddressName(dto.addressName());
        place.setAddressComment(dto.addressComment());
        return place;
    }
}
