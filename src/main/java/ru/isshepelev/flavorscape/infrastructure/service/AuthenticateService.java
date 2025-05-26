package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.ui.dto.ChangePasswordDto;
import ru.isshepelev.flavorscape.ui.dto.SignInDto;
import ru.isshepelev.flavorscape.ui.dto.SignUpDto;

public interface AuthenticateService {

    void signUp(SignUpDto signUp);

    void signIn(SignInDto signIn);

    void changePassword(ChangePasswordDto changePasswordDto, String name);
}
