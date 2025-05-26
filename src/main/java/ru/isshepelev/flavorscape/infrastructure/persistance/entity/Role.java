package ru.isshepelev.flavorscape.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Role{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
