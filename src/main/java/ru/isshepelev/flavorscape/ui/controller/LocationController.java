package ru.isshepelev.flavorscape.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.flavorscape.infrastructure.service.Gis2Service;
import ru.isshepelev.flavorscape.ui.dto.ApiConfigDto;


@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final Gis2Service gis2Service;

    @PostMapping("/receive")
    public ResponseEntity<?> receiveLocation(@RequestBody ApiConfigDto config) {
        return ResponseEntity.ok(gis2Service.searchOrganizations(config));
    }

}
