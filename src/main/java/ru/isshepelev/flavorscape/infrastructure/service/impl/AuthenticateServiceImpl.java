package ru.isshepelev.flavorscape.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.exception.InvalidOldPasswordException;
import ru.isshepelev.flavorscape.infrastructure.exception.PasswordsNotMatchException;
import ru.isshepelev.flavorscape.infrastructure.exception.UsernameAlreadyExistsException;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Role;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.RoleRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserRepository;
import ru.isshepelev.flavorscape.infrastructure.service.AuthenticateService;
import ru.isshepelev.flavorscape.ui.dto.ChangePasswordDto;
import ru.isshepelev.flavorscape.ui.dto.SignInDto;
import ru.isshepelev.flavorscape.ui.dto.SignUpDto;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Override
    public void signUp(SignUpDto signUp) {
        if (!signUp.getPassword().equals(signUp.getRepeatPassword())){
            throw new PasswordsNotMatchException("Passwords do not match");
        }
        if (userRepository.existsByUsername(signUp.getUsername())){
            throw new UsernameAlreadyExistsException("User already exist");
        }

        Role roleUser = roleRepository.findByName("ROLE_USER"); //TODO переделать добавление роли при регистрации

        User user = new User();
        user.setPassword(passwordEncoder.encode(signUp.getPassword()));
        user.setUsername(signUp.getUsername());
        user.setRoles(Set.of(roleUser));
        userRepository.save(user);
    }

    @Override
    public void signIn(SignInDto signIn) {
        User user = userRepository.findByUsername(signIn.getUsername());
        if (user == null){
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(signIn.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signIn.getUsername(),
                signIn.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new InvalidOldPasswordException("The initial password is incorrect");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getRepeatPassword())) {
            throw new PasswordsNotMatchException("New password and repeat password do not match");
        }

        if (passwordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Новый пароль должен отличаться от старого");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        changePasswordDto.getNewPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
