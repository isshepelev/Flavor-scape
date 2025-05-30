package ru.isshepelev.flavorscape.infrastructure.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Place;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.PlaceRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserRepository;
import ru.isshepelev.flavorscape.infrastructure.service.PlaceService;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendDto;
import ru.isshepelev.flavorscape.infrastructure.service.dto.OrganizationDto;
import ru.isshepelev.flavorscape.ui.dto.NotificationRequestDto;
import ru.isshepelev.flavorscape.ui.dto.PlaceRequestDto;
import ru.isshepelev.flavorscape.ui.dto.PlaceReviewDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private UserRepository userRepository;

    @Override
    public void savePlacesToDatabase(List<OrganizationDto> organizationDto){
        if (organizationDto == null) {
            throw new IllegalArgumentException("Organization DTO list cannot be null");
        }

        if (organizationDto.isEmpty()) {
            return;
        }

        List<Place> places = organizationDto.stream().filter(Objects::nonNull).map(this::convertToEntity).toList();
        placeRepository.saveAll(places);

    }

    @Override
    public PlaceRequestDto getPlaceById(Long placeId, String username) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));

        PlaceRequestDto dto = new PlaceRequestDto();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setAddressName(place.getAddressName());
        dto.setAddressComment(place.getAddressComment());

        List<PlaceReviewDto> reviewDtos = place.getReviews().stream()
                .map(review -> {
                    PlaceReviewDto reviewDto = new PlaceReviewDto();
                    reviewDto.setId(review.getId());
                    reviewDto.setGeneralImpression(review.getGeneralImpression());
                    reviewDto.setCritique(review.getCritique());
                    reviewDto.setGeneralRating(review.getGeneralRating());
                    reviewDto.setContent(review.getContent());

                    reviewDto.setAuthorId(review.getAuthor().getId());
                        reviewDto.setAuthorName(review.getAuthor().getUsername());

                    return reviewDto;
                }).toList();

        dto.setPlaceReviewDto(reviewDtos);

        return dto;
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
