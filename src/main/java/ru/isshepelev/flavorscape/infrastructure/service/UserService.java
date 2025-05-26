package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.ui.dto.SignInDto;
import ru.isshepelev.flavorscape.ui.dto.SignUpDto;

public interface UserService {

    void signUp(SignUpDto signUp);

    void signIn(SignInDto signIn);
}
