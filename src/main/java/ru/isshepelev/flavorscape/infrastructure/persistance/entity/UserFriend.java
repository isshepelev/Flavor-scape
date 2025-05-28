package ru.isshepelev.flavorscape.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.enums.FriendStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_friends")
public class UserFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    private LocalDateTime createdAt;

}