package com.urssa.urssaAppPressing.v2.auth.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "username_cannot_be_null")
    private String username;

    @NotBlank(message = "password_cannot_be_null")
    String password;
}
