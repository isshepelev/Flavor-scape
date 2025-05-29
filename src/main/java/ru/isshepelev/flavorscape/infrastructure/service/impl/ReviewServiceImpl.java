package ru.isshepelev.flavorscape.infrastructure.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.*;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.PlaceRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.ReviewRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserRepository;
import ru.isshepelev.flavorscape.infrastructure.service.ReviewService;
import ru.isshepelev.flavorscape.infrastructure.service.dto.ReviewRequestDto;
import ru.isshepelev.flavorscape.ui.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Override
    public void createReview(Long placeId, ReviewDto reviewDto, String username) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new EntityNotFoundException("place not found"));
        User user = userRepository.findByUsername(username);
        if (user == null) throw new EntityNotFoundException("user not found");

        boolean hasExistingReview = reviewRepository.existsByAuthorAndPlace(user, place);
        if (hasExistingReview) {
            throw new IllegalStateException("User has already reviewed this place");
        }

        Review review = new Review();
        review.setCreatedAt(LocalDateTime.now());
        review.setGeneralImpression(reviewDto.generalImpression());
        review.setCritique(reviewDto.critique());
        review.setGeneralRating(generateGeneralRating(reviewDto.critique()));
        review.setContent(reviewDto.content());
        review.setAuthor(user);
        review.setPlace(place);

        reviewRepository.save(review);

        place.getReviews().add(review);
        placeRepository.save(place);
    }

    @Override
    public List<ReviewRequestDto> getAllReviewsForPlace(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));

        return reviewRepository.findAllByPlaceOrderByCreatedAtDesc(place).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public List<ReviewRequestDto> getFriendsReviewsForPlace(Long placeId, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));

        Set<User> friends = new HashSet<>();
        user.getFriends().forEach(userFriend -> friends.add(userFriend.getFriend()));
        user.getFriendOf().forEach(userFriend -> friends.add(userFriend.getUser()));

        return reviewRepository.findAllByPlaceAndAuthorInOrderByCreatedAtDesc(place, friends).stream()
                .map(this::convertToDto)
                .toList();
    }

    private ReviewRequestDto convertToDto(Review review) {
        return new ReviewRequestDto(
                review.getId(),
                review.getCreatedAt(),
                review.getGeneralImpression(),
                review.getCritique(),
                review.getGeneralRating(),
                review.getContent(),
                review.getAuthor().getUsername()
        );
    }

    private double generateGeneralRating(Critique critique) {
        return Math.round((critique.getMusic() + critique.getPoliteness() + critique.getPurity()
                + critique.getTasteOfFood() + critique.getPrice()) * 2) / 10.0;
    }
}
