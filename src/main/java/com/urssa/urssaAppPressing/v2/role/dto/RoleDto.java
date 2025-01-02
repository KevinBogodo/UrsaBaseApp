package com.urssa.urssaAppPressing.v2.role.dto;

import com.urssa.urssaAppPressing.v2.permission.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RoleDto {

    @NotNull(message = "id_cannot_be_null")
    private UUID id;

    @NotBlank(message = "name_cannot_be_empty")
    private String name;

    private Boolean isAdmin;

    private Set<Permission> permissions;

}
