package com.urssa.urssaAppPressing.v2.permission;

import com.urssa.urssaAppPressing.v2.permission.dto.AddPermissionDto;
import com.urssa.urssaAppPressing.v2.permission.dto.PermissionDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
public class PermissionMapper {

    private final Validator validator;

    public PermissionMapper() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    public PermissionDto convertToDto(Permission permission) {
        if (permission == null) {
            throw new NullPointerException("The permission is null");
        }

        PermissionDto dto = new PermissionDto(
                permission.getId(),
                permission.getName(),
                permission.getCode(),
                permission.getCreatedAt(),
                permission.getUpdatedAt()
        );

        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .reduce((msg1, msg2)-> msg1 )
                            .orElse("validation failed")
            );
        }

        return dto;
    }


    public Permission convertToPermission(AddPermissionDto request) {
        if (request == null) {
            throw new NullPointerException("The permissionDto is null");
        }
        var permission = new Permission(
                request.getName(),
                request.getCode()
        );
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .reduce((msg1, msg2)-> msg1)
                            .orElse("validation failed")
            );
        }

        return permission;
    }
}
