package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Review;
import ru.isshepelev.flavorscape.infrastructure.service.dto.ReviewRequestDto;
import ru.isshepelev.flavorscape.ui.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    void createReview(Long placeId, ReviewDto reviewDto, String username);


    List<ReviewRequestDto> getAllReviewsForPlace(Long placeId);

    List<ReviewRequestDto> getFriendsReviewsForPlace(Long placeId, String username);
}
