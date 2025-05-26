package ru.isshepelev.flavorscape.ui.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.flavorscape.infrastructure.exception.InvalidOldPasswordException;
import ru.isshepelev.flavorscape.infrastructure.exception.PasswordsNotMatchException;
import ru.isshepelev.flavorscape.infrastructure.exception.UsernameAlreadyExistsException;
import ru.isshepelev.flavorscape.infrastructure.service.AuthenticateService;
import ru.isshepelev.flavorscape.infrastructure.service.UserService;
import ru.isshepelev.flavorscape.ui.dto.ChangePasswordDto;
import ru.isshepelev.flavorscape.ui.dto.SignInDto;
import ru.isshepelev.flavorscape.ui.dto.SignUpDto;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateService authenticateService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUp) {
        try {
            authenticateService.signUp(signUp);
            return ResponseEntity.ok().build();
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь уже существует");
        } catch (PasswordsNotMatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пароли не совпадают");
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> loginUser(@RequestBody SignInDto signIn) {
        try {
            authenticateService.signIn(signIn);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Неверный логин или пароль");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            authenticateService.changePassword(changePasswordDto, principal.getName());
            return ResponseEntity.ok().build();
        } catch (PasswordsNotMatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пароли не совпадают");
        } catch (InvalidOldPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Изначальный пароль неверный");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Новый пароль должен отличаться от старого");
        }
    }
}
