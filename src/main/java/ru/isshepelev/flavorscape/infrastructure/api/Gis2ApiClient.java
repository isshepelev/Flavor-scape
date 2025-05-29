package ru.isshepelev.flavorscape.infrastructure.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "gis2Client", url = "https://catalog.api.2gis.com/3.0/items")
public interface Gis2ApiClient {

    @GetMapping
    String searchOrganizations(
            @RequestParam("q") String query,
            @RequestParam("point") String point,
            @RequestParam("radius") String radius,
            @RequestParam("key") String key,
            @RequestParam(value = "type", defaultValue = "branch") String type
    );

    @GetMapping
    String searchOrganizationsWithWorkTime(
            @RequestParam("q") String query,
            @RequestParam("point") String point,
            @RequestParam("radius") String radius,
            @RequestParam("work_time") String workTime,
            @RequestParam("key") String key,
            @RequestParam(value = "type", defaultValue = "branch") String type
    );

}