package com.urssa.urssaAppPressing.v2.user.dto;

import com.urssa.urssaAppPressing.v2.role.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AddUserDto {

    @NotBlank(message = "name_cannot_be_null")
    private String firstName;

    private String lastName;

    private Date dob;

    private String sex;

    private String email;

    @NotBlank(message = "username_cannot_be_empty")
    private String username;

    @NotBlank(message = "password_cannot_be_empty")
    private String password;

    private String phone;

    private UUID role;
}
