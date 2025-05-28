package ru.isshepelev.flavorscape.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.PlaceRepository;
import ru.isshepelev.flavorscape.infrastructure.service.PlaceService;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

}
