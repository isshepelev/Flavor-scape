package ru.isshepelev.flavorscape.infrastructure.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrganizationListDto implements Serializable {
    private List<OrganizationDto> organizations;
}