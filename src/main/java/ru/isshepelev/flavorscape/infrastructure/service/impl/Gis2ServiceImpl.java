package ru.isshepelev.flavorscape.infrastructure.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.api.Gis2ApiClient;
import ru.isshepelev.flavorscape.infrastructure.service.Gis2Service;
import ru.isshepelev.flavorscape.infrastructure.service.dto.OrganizationDto;
import ru.isshepelev.flavorscape.infrastructure.service.dto.OrganizationListDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Gis2ServiceImpl implements Gis2Service {

    private final Gis2ApiClient gis2ApiClient;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, OrganizationListDto> kafkaTemplate;

    @Value("${api_key.2gis}")
    private String apiKey;
    @Value("${spring.kafka.topic.organizations}")
    private String organizationTopic;

    @Override
    public List<OrganizationDto> searchOrganizations(String placeName, String coordinates, String radius, boolean workTime) {
        String response;
        if (workTime) {
            response = gis2ApiClient.searchOrganizationsWithWorkTime(
                    placeName, coordinates, radius, "now", apiKey, "branch");
        } else {
            response = gis2ApiClient.searchOrganizations(
                    placeName, coordinates, radius, apiKey, "branch");
        }

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode itemsNode = root.path("result").path("items");

            List<OrganizationDto> organizations = new ArrayList<>();
            for (JsonNode item : itemsNode) {
                organizations.add(new OrganizationDto(
                        item.path("id").asLong(),
                        item.path("name").asText(),
                        item.path("address_name").asText(),
                        item.path("address_comment").asText(null)
                ));
            }

            OrganizationListDto organizationListDto = new OrganizationListDto();
            organizationListDto.setOrganizations(organizations);

            kafkaTemplate.send(organizationTopic, organizationListDto);

            return organizations;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse 2GIS response", e);
        }
    }
}
