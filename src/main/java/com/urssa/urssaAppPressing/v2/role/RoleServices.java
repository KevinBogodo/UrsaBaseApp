package com.urssa.urssaAppPressing.v2.role;

import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.role.dto.AddRoleDto;
import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;

import java.util.List;
import java.util.UUID;

public interface RoleServices {

    List<RoleDto> loadOrSearchNotAdminActiveRoles(String term);

    PageResponse<RoleDto> loadOrSearchNotAdminActiveRolesPaged(Long page, Long size, String term);

    RoleDto loadRoleById(UUID id);

    RoleDto addRoles(AddRoleDto request, String type);

    RoleDto updateRole(AddRoleDto request, UUID id);

    String softDeleteRole(UUID id);

    RoleDto restoreRole(UUID id);

    RoleDto changeRoleType(UUID id,String type);


    List<RoleDto> loadOrSearchAdminActiveRoles(String term);

    PageResponse<RoleDto> loadOrSearchAdminActiveRolesPaged(Long page, Long size, String term);

    PageResponse<RoleDto> loadOrSearchAdminNotActiveRolesPaged(Long page, Long size, String term);

    PageResponse<RoleDto> loadOrSearchNotAdminNotActiveRolesPaged(Long page, Long size, String term);

    String deleteRole(UUID id);



}
