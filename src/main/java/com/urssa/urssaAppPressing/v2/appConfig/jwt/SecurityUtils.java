package com.urssa.urssaAppPressing.v2.appConfig.jwt;

import com.urssa.urssaAppPressing.v2.user.User;
import com.urssa.urssaAppPressing.v2.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> user = userRepository.findByUsername(authentication.getName());
            return user.map(User::getId).orElse(null);
        }

        return null;
    }
}
