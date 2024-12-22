package com.urssa.urssaAppPressing.v2.permission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddPermissionDto {

    @NotNull(message = "name_cannot_be_null")
    @NotBlank(message = "name_cannot_be_empty")
    private String name;

    @NotNull(message = "code_cannot_be_null")
    @NotBlank(message = "code_cannot_be_empty")
    private String code;
}
