package com.urssa.urssaAppPressing.v2.role;

import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.role.dto.AddRoleDto;
import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;

import java.util.List;
import java.util.UUID;

public interface RoleServices {

    List<RoleDto> loadOrSearchActiveRoles(String term);

    PageResponse<RoleDto> loadOrSearchActiveRolesPaged(Long page, Long size, String term);

    List<RoleDto> loadOrSearchRoles(String term);

    PageResponse<RoleDto> loadOrSearchRolesPaged(Long page, Long size, String term);

    RoleDto loadRoleById(UUID id);

    RoleDto addRoles(AddRoleDto request);

    RoleDto updateRole(AddRoleDto request, UUID id);

    String softDeleteRole(UUID id);

    String deleteRole(UUID id);

    Role setAdminRole(UUID id);

}
