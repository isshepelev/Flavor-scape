package ru.isshepelev.flavorscape;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final Gis2ApiClient gis2ApiClient;

    @Value("${api_key.2gis}")
    private String apiKey;

    public void searchOrganizations(String placeName, String coordinates, String radius, boolean workTime) {
        String response;
        if (workTime) {
            response = gis2ApiClient.searchOrganizationsWithWorkTime(placeName, coordinates, radius, "now", apiKey, "branch");
        } else {
            response = gis2ApiClient.searchOrganizations(placeName, coordinates, radius, apiKey, "branch");
        }
        System.out.println(response);
    }
}
