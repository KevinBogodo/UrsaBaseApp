package com.urssa.urssaAppPressing.v2.appConfig.entity;

import com.urssa.urssaAppPressing.v2.appConfig.jwt.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class AuditorAwareImpl implements AuditorAware<UUID> {

    private final SecurityUtils securityUtils;

    public AuditorAwareImpl(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.ofNullable(securityUtils.getCurrentUserId());
    }
}
