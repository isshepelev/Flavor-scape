package ru.isshepelev.flavorscape.ui.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.flavorscape.infrastructure.api.GenerateReview;
import ru.isshepelev.flavorscape.infrastructure.service.ReviewService;
import ru.isshepelev.flavorscape.ui.dto.ReviewDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place/{placeId}/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final GenerateReview generateReview;

    @PostMapping
    public ResponseEntity<?> createReview(@PathVariable Long placeId, @RequestBody ReviewDto reviewDto, @AuthenticationPrincipal UserDetails userDetails){
        try {
            reviewService.createReview(placeId, reviewDto, userDetails.getUsername());
            return ResponseEntity.ok().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body("Пользователь или заведение не найдено");
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body("Вы уже оставили отзыв");
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllReview(@PathVariable Long placeId){
        try {
            return ResponseEntity.ok(reviewService.getAllReviewsForPlace(placeId));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body("Заведение не найдено");
        }
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriendsReview(@PathVariable Long placeId, @AuthenticationPrincipal UserDetails userDetails){
        try {
            return ResponseEntity.ok(reviewService.getFriendsReviewsForPlace(placeId, userDetails.getUsername()));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body("Заведение или пользователь не найдены");
        }
    }

    @PostMapping("/ai")
    public ResponseEntity<?> generateAIReview(@PathVariable Long placeId, @AuthenticationPrincipal UserDetails userDetails){
        try {
            return ResponseEntity.ok(generateReview.generateAIReview(placeId, userDetails.getUsername()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка при генерации отчета ИИ");
        }
    }

}
