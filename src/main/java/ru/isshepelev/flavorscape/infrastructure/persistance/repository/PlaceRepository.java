package ru.isshepelev.flavorscape.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

}
