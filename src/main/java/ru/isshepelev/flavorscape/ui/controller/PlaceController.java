package ru.isshepelev.flavorscape.ui.controller;

import feign.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isshepelev.flavorscape.infrastructure.service.PlaceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ResponseEntity<?> getPlaceById(@PathVariable Long placeId, @AuthenticationPrincipal UserDetails userDetails){
        try {
            return ResponseEntity.ok(placeService.getPlaceById(placeId, userDetails.getUsername()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Заведение не найдено");
        }
    }

}
