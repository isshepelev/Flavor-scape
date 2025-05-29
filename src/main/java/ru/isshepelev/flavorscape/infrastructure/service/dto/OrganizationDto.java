package ru.isshepelev.flavorscape.infrastructure.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record OrganizationDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("address_name") String addressName,
        @JsonProperty("address_comment") String addressComment
) implements Serializable {

}
