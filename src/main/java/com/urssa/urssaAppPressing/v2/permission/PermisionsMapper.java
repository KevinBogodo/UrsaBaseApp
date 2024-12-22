package com.urssa.urssaAppPressing.v2.permission;

import com.urssa.urssaAppPressing.v2.permission.dto.AddPermissionDto;
import com.urssa.urssaAppPressing.v2.permission.dto.PermissionDto;
import org.springframework.stereotype.Service;


@Service
public class PermisionsMapper {

    public PermissionDto convertToDto(Permission permission) {

        return new PermissionDto(
                permission.getId(),
                permission.getName(),
                permission.getCode(),
                permission.getCreatedBy(),
                permission.getCreatedAt(),
                permission.getUpdatedAt());
    }

    public Permission convertToPermission(AddPermissionDto request) {
        return new Permission(
                request.getName(),
                request.getCode()
        );
    }
}
