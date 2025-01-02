package com.urssa.urssaAppPressing.v2.role;


import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;

@Service
public class RoleMapper {

    private  final Validator validator;

    public RoleMapper() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    public RoleDto convertToRoleDto(Role role) {
        if (role == null) {
            throw new NullPointerException("the_role_is_null");
        }

        RoleDto dto = new RoleDto(
                role.getId(),
                role.getName(),
                role.isAdmin(),
                role.getPermissions()
        );

        System.out.println("Permissions in RoleDto: " + dto.getPermissions());


        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .reduce((msg1, msg2)->msg1)
                            .orElse("validation_failed")
            );
        }

        return dto;
    }

}
