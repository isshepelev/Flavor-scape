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
import ru.isshepelev.flavorscape.infrastructure.service.FriendService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request/{recipientId}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long recipientId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            friendService.sendFriendRequest(userDetails.getUsername(), recipientId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        } catch (FriendRequestAlreadySentException e) {
            return ResponseEntity.badRequest().body("Запрос уже отправлен");
        } catch (AlreadyFriendsException e) {
            return ResponseEntity.badRequest().body("Уже у вас в друзьях");
        } catch (UserBlockedException e) {
            return ResponseEntity.badRequest().body("Пользователь заблокировал вас");
        }
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            friendService.acceptFriendRequest(requestId,userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e ){
            return ResponseEntity.badRequest().body("Пользователь не найден");
        } catch (SecurityException e ){
            return ResponseEntity.badRequest().body("Вы не можете принять этот запрос");
        } catch (AlreadyFriendsException e ){
            return ResponseEntity.badRequest().body("Уже у вас в друзьях");
        } catch (IllegalStateException e ){
            return ResponseEntity.badRequest().body("Запрос на добавление в друзья не находится в статусе ожидания");
        }
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            friendService.rejectFriendRequest(requestId, userDetails.getUsername());
            return ResponseEntity.ok().build();
        }catch (EntityNotFoundException e ) {
            return ResponseEntity.badRequest().body("Запрос на дружбу не найден");
        }catch (SecurityException e ){
            return ResponseEntity.badRequest().body("Вы не можете принять этот запрос");
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body("Невозможно отклонить уже принятый запрос в друзья");
        }
    }

    @PostMapping("/blocked/{requestId}")
    public ResponseEntity<?> blockedUserRequest(@PathVariable Long requestId, @AuthenticationPrincipal UserDetails userDetails){
        try {
            friendService.blockedUserRequest(requestId, userDetails.getUsername());
            return ResponseEntity.ok().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
    }

    @PostMapping("/remove/{requestId}")
    public ResponseEntity<?> removeFriendRequest(@PathVariable Long requestId, @AuthenticationPrincipal UserDetails userDetails){
        try {
            friendService.removeFromFriendsRequest(requestId, userDetails.getUsername());
            return ResponseEntity.ok().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body("Этот пользователь не в вашем списке друзей");
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
