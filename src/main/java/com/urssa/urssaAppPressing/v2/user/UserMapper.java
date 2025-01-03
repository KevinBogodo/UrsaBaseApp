package com.urssa.urssaAppPressing.v2.user;

import com.urssa.urssaAppPressing.v2.user.dto.AddUserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UpdateUserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    private final Validator validator;

    public UserMapper() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            this.validator = factory.getValidator();
        }
    }

    public UserDto convertToUserDto(User user) {
        if (user == null) {
            throw new NullPointerException("user_is_null");
        }

        UserDto dto = new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getDob(),
                user.getSex(),
                user.getEmail(),
                user.getUsername(),
                user.getPhone(),
                user.getRole()
        );

        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .reduce((msg1, msg2) -> msg1)
                            .orElse("validation_failed")
            );
        }

        return dto;
    }


    public User convertToUser(AddUserDto request) {
        if (request == null){
            throw new NullPointerException("request_to_convert_is_null");
        }
        User user =  User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .sex(request.getSex())
                .email(request.getEmail())
                .username(request.getUsername())
                .phone(request.getPhone())
                .build();

        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .reduce((msg1, msg2) -> msg1)
                            .orElse("validation_failed")
            );
        }

        return user;

    }

    public User mapToUser(UpdateUserDto request, User user) {
        if (request == null){
            throw new NullPointerException("request_to_convert_is_null");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        user.setSex(request.getSex());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());

        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .reduce((msg1, msg2) -> msg1)
                            .orElse("validation_failed")
            );
        }

        return user;

    }

}
