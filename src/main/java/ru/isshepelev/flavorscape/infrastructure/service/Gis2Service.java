package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.infrastructure.service.dto.OrganizationDto;

import java.util.List;

public interface Gis2Service {

    List<OrganizationDto> searchOrganizations(String placeName, String coordinates, String radius, boolean workTime);
}
