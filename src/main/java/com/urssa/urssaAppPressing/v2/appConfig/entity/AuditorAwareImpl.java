package com.urssa.urssaAppPressing.v2.appConfig.entity;

import com.urssa.urssaAppPressing.v2.appConfig.jwt.SecurityUtils;

import java.util.Optional;
import java.util.UUID;

public class AuditorAwareImpl {

    @Override
    public Optional<UUID> getCurrentAudior() {
        return Optional.ofNullable(SecurityUtils.getCurrentUserId());
    }
}
