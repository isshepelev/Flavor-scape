package ru.isshepelev.flavorscape.ui.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.flavorscape.infrastructure.exception.PasswordsNotMatchException;
import ru.isshepelev.flavorscape.infrastructure.exception.UsernameAlreadyExistsException;
import ru.isshepelev.flavorscape.infrastructure.service.UserService;
import ru.isshepelev.flavorscape.ui.dto.SignInDto;
import ru.isshepelev.flavorscape.ui.dto.SignUpDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUp){
        try {
            userService.signUp(signUp);
            return ResponseEntity.ok().build();
        }catch (UsernameAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь уже существует");
        }catch (PasswordsNotMatchException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пароли не совпадают");
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> loginUser(@RequestBody SignInDto signIn){
        try {
            userService.signIn(signIn);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body("Неверный логин или пароль");
        }
    }
}
