package com.urssa.urssaAppPressing.v2.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AddRoleDto {

    @NotBlank(message = "name_cannot_be_null")
    private String name;

    private List<UUID> permissions;

}
