package com.urssa.urssaAppPressing.v2.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AddRoleDto {

    @NotBlank(message = "name_cannot_be_null")
    private String name;

    private Set<UUID> permissions;

}
