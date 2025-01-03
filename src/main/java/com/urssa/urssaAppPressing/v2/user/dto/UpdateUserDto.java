package com.urssa.urssaAppPressing.v2.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateUserDto {

    @NotBlank(message = "name_cannot_be_null")
    private String firstName;

    private String lastName;

    private Date dob;

    private String sex;

    private String email;

    @NotBlank(message = "username_cannot_be_empty")
    private String username;

    private String phone;

    private UUID role;
}
