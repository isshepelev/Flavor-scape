package ru.isshepelev.flavorscape.ui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.flavorscape.infrastructure.service.dto.LocationDto;
import ru.isshepelev.flavorscape.infrastructure.service.Gis2Service;
import ru.isshepelev.flavorscape.infrastructure.service.PlaceService;


@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final Gis2Service gis2Service;

    @PostMapping("/receive")
    public ResponseEntity<?> receiveLocation(@RequestBody LocationDto location) {

        String coordinates = location.getLongitude() + "%2C" + location.getLatitude();
        String testCoordinat = "39.18444%2C51.68178";
        String radius = "5000";
        String placeName = "кафе";
        boolean workTime = false;


        return ResponseEntity.ok(gis2Service.searchOrganizations(placeName, coordinates, radius, workTime));

    }

}
