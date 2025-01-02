package com.urssa.urssaAppPressing.v2.permission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddPermissionDto {

    @NotBlank(message = "name_cannot_be_empty")
    private String name;

    @NotBlank(message = "code_cannot_be_empty")
    private String code;

}
