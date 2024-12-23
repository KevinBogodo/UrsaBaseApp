package com.urssa.urssaAppPressing.v2.auth.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "firstName_cannot_be_null")
    private String firstName;

    private String lastName;

    @NotBlank(message = "sex_cannot_null")
    private String sex;

    private String email;

    @NotBlank(message = "username_cannot_be_null")
    private String username;

    @NotBlank(message = "password_cannot_be_null")
    private String password;

    private String phone;

    private UUID role;
}
