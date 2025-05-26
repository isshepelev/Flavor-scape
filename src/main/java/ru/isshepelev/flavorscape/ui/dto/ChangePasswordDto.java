package ru.isshepelev.flavorscape.ui.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {

    private String oldPassword;
    private String newPassword;
    private String repeatPassword;

}
