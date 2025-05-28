package ru.isshepelev.flavorscape.infrastructure.persistance.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Critique {

    private short tasteOfFood;
    private short politeness;
    private short price;
    private short purity;
    private short music;

}
