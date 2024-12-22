package com.urssa.urssaAppPressing.v2.auth.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String sex;
    private String email;
    private String username;
    private String password;
    private String phone;
    private Long role;
}
