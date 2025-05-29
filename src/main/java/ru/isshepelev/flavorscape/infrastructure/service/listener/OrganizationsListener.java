package ru.isshepelev.flavorscape.infrastructure.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.isshepelev.flavorscape.infrastructure.service.PlaceService;
import ru.isshepelev.flavorscape.infrastructure.service.dto.OrganizationListDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrganizationsListener {

    private final PlaceService placeService;


    @SneakyThrows
    @KafkaListener(topics = "${spring.kafka.topic.organizations}")
    public void listenOrganizations(OrganizationListDto organizationListDto) {
        placeService.savePlacesToDatabase(organizationListDto.getOrganizations());
    }

}