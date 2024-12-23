package com.urssa.urssaAppPressing.v2.auth;

import com.urssa.urssaAppPressing.v2.appConfig.jwt.JwtService;
import com.urssa.urssaAppPressing.v2.role.RoleRepository;
import com.urssa.urssaAppPressing.v2.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }
}