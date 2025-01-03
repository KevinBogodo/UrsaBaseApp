package com.urssa.urssaAppPressing.v2.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordDto {

    @NotBlank(message = "old_password_cannot_be_blank")
    private String oldPassword;

    @NotBlank(message = "new_password_cannot_be_blank")
    private String newPassword;


}
