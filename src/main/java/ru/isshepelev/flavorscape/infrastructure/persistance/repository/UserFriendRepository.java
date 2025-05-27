package ru.isshepelev.flavorscape.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.UserFriend;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

    boolean existsByUserAndFriendAndStatus(User user, User friend, UserFriend.FriendStatus status);

    Optional<UserFriend> findByUserAndFriend(User user, User friend);

    boolean existsByUserAndFriend(User user, User friend);

    List<UserFriend> findByUserAndStatus(User user, UserFriend.FriendStatus status);

    List<UserFriend> findByFriendAndStatus(User friend, UserFriend.FriendStatus status);
}