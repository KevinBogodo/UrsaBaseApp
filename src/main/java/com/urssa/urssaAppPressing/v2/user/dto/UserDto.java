package com.urssa.urssaAppPressing.v2.user.dto;

import com.urssa.urssaAppPressing.v2.role.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Data
public class UserDto {

    @NotNull(message = "id_cannot_be_null")
    private UUID id;

    @NotBlank(message = "firstName_cannot_be_empty")
    private String firstName;

    private String lastName;

    private Date dob;

    private String sex;

    private String email;

    @NotBlank(message = "username_cannot_be_empty")
    private String username;

    private String phone;

    private Role role;
}
