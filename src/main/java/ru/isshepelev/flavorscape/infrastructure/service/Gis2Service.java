package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.infrastructure.service.dto.OrganizationDto;
import ru.isshepelev.flavorscape.ui.dto.ApiConfigDto;

import java.util.List;

public interface Gis2Service {

    List<OrganizationDto> searchOrganizations(ApiConfigDto configDto);
}
