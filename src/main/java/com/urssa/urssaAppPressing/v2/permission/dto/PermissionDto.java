package com.urssa.urssaAppPressing.v2.permission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PermissionDto {

    @NotNull(message = "id_cannot_be_null")
    private UUID id;

    @NotBlank(message = "name_cannot_be_empty")
    private String name;

    @NotBlank(message = "code_cannot_be_empty")
    private String code;

}
