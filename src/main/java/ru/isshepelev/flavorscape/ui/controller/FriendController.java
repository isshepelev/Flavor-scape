package ru.isshepelev.flavorscape.ui.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.flavorscape.infrastructure.exception.AlreadyFriendsException;
import ru.isshepelev.flavorscape.infrastructure.exception.FriendRequestAlreadySentException;
import ru.isshepelev.flavorscape.infrastructure.exception.UserBlockedException;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.UserFriend;
import ru.isshepelev.flavorscape.infrastructure.service.FriendService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request/{recipientId}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long recipientId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            friendService.sendFriendRequest(Long.valueOf(userDetails.getUsername()), recipientId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        } catch (FriendRequestAlreadySentException e) {
            return ResponseEntity.badRequest().body("Запрос уже отправлен");
        } catch (AlreadyFriendsException e) {
            return ResponseEntity.badRequest().body("Уже у вас в друзьях");
        } catch (UserBlockedException e) {
            ResponseEntity.badRequest().body("Пользователь заблокировал вас");
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            friendService.acceptFriendRequest(requestId, Long.valueOf(userDetails.getUsername()));
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e ){
            return ResponseEntity.badRequest().body("Пользователь не найден");
        } catch (SecurityException e ){
            return ResponseEntity.badRequest().body("Вы не можете принять этот запрос");
        } catch (AlreadyFriendsException e ){
            return ResponseEntity.badRequest().body("Уже у вас в друзьях");
        }
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            friendService.rejectFriendRequest(requestId, Long.valueOf(userDetails.getUsername()));
            return ResponseEntity.ok().build();
        }catch (EntityNotFoundException e ) {
            return ResponseEntity.badRequest().body("Запрос на дружбу не найден");
        }catch (SecurityException e ){
            return ResponseEntity.badRequest().body("Вы не можете принять этот запрос");
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRequests(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(friendService.getPendingRequests(Long.valueOf(userDetails.getUsername())));
    }

    @GetMapping
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(friendService.getFriends(Long.valueOf(userDetails.getUsername())));
    }
}
