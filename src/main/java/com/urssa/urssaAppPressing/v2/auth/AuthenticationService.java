package com.urssa.urssaAppPressing.v2.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urssa.urssaAppPressing.v2.appConfig.jwt.JwtService;
import com.urssa.urssaAppPressing.v2.appConfig.response.AuthenticationResponse;
import com.urssa.urssaAppPressing.v2.auth.request.AuthenticationRequest;
import com.urssa.urssaAppPressing.v2.auth.request.RegisterRequest;
import com.urssa.urssaAppPressing.v2.role.RoleRepository;
import com.urssa.urssaAppPressing.v2.user.User;
import com.urssa.urssaAppPressing.v2.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {

        var role = roleRepository.findById(request.getRole())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .sex(request.getSex())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(role)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );


        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final  String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;

        System.out.println("----------------************************-------------");

        if(authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userName =  jwtService.extractUsername(refreshToken);
        System.out.println("userName: "+userName);
        if(userName != null) {
            var user = this.userRepository.findByUsername(userName).orElseThrow();
            System.out.println("authHeader: "+user);
            if(jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponse
                        .builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }
    }
}
