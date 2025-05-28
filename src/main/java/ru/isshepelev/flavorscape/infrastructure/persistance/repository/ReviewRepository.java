package ru.isshepelev.flavorscape.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Place;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Review;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByAuthorAndPlace(User author, Place place);

    List<Review> findAllByPlaceOrderByCreatedAtDesc(Place place);

    List<Review> findAllByPlaceAndAuthorInOrderByCreatedAtDesc(Place place, Collection<User> authors);
}
