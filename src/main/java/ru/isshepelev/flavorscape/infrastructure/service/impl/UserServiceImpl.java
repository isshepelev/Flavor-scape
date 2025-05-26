package ru.isshepelev.flavorscape.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.exception.PasswordsNotMatchException;
import ru.isshepelev.flavorscape.infrastructure.exception.UsernameAlreadyExistsException;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserRepository;
import ru.isshepelev.flavorscape.infrastructure.service.UserService;
import ru.isshepelev.flavorscape.ui.dto.SignInDto;
import ru.isshepelev.flavorscape.ui.dto.SignUpDto;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


}
