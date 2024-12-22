package com.urssa.urssaAppPressing.v2.permission;

import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.permission.dto.AddPermissionDto;
import com.urssa.urssaAppPressing.v2.permission.dto.PermissionDto;

import java.util.List;
import java.util.UUID;

public interface PermissionServices {

    List<PermissionDto> loadOrSearchAllPermission(String term);
    PageResponse<PermissionDto> loadOrSearchAllPermissionPaginated(Long page, Long size, String term);
    PermissionDto loadPermissionById(UUID id);
    PermissionDto addPermission(AddPermissionDto addPermissionDTO);
    PermissionDto updatePermission(AddPermissionDto updatedData, UUID id);
}
