package com.urssa.urssaAppPressing.v2.role;


import com.urssa.urssaAppPressing.v2.appConfig.response.ApiResponseOk;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.role.dto.AddRoleDto;
import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/access")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServicesImpl roleServices;

    @GetMapping("/roles/search")
    public ResponseEntity<ApiResponseOk> loadOrSearchNotAdminActiveRoles(@RequestParam(defaultValue = "") String term) {

        List<RoleDto> roles = roleServices.loadOrSearchNotAdminActiveRoles(term);
        return  ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponseOk> loadOrSearchNotAdminActiveRolesPaged(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") String term
    ){
        PageResponse<RoleDto> roles = roleServices.loadOrSearchNotAdminActiveRolesPaged(page, size, term);
        return ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<ApiResponseOk> loadRoleById(@PathVariable UUID id) {
        RoleDto role = roleServices.loadRoleById(id);
        return ResponseEntity.ok(new ApiResponseOk("success", role));
    }

    @PostMapping("/roles")
    public ResponseEntity<ApiResponseOk> createRole (
            @Validated @RequestBody AddRoleDto request,
            @RequestParam(defaultValue = "client") String type
    ) {
        RoleDto role = roleServices.addRoles(request, type);
        return ResponseEntity.ok(new ApiResponseOk("success", role));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<ApiResponseOk> updateRole (@Validated @RequestBody AddRoleDto request, @PathVariable UUID id) {
        RoleDto role = roleServices.updateRole(request, id);
        return ResponseEntity.ok(new ApiResponseOk("success", role));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<ApiResponseOk> deleteActiveRole(@PathVariable UUID id) {
        String message = roleServices.softDeleteRole(id);
        return ResponseEntity.ok(new ApiResponseOk(message, null));
    }

    /***************************** Admin *****************************************/

    @PostMapping("/admin/roles/restore/{id}")
    public ResponseEntity<ApiResponseOk> restoreDeletedRole(@PathVariable UUID id) {
        RoleDto roleDto = roleServices.restoreRole(id);
        return ResponseEntity.ok(new ApiResponseOk("success", roleDto));
    }

    @PostMapping("/admin/roles/change-type/{id}/{type}")
    public ResponseEntity<ApiResponseOk> changeRoleType(
            @PathVariable UUID id,
            @PathVariable String type
    ) {
        RoleDto roleDto = roleServices.changeRoleType(id, type);
        return ResponseEntity.ok(new ApiResponseOk("success", roleDto));
    }

    @GetMapping("/admin/roles/search")
    public ResponseEntity<ApiResponseOk> loadOrSearchAdminActiveRoles(@RequestParam(defaultValue = "") String term) {

        List<RoleDto> roles = roleServices.loadOrSearchAdminActiveRoles(term);
        return  ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @GetMapping("/admin/roles")
    public ResponseEntity<ApiResponseOk> loadOrSearchAdminActiveRolesPaged(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") String term
    ){
        PageResponse<RoleDto> roles = roleServices.loadOrSearchAdminActiveRolesPaged(page, size, term);
        return ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @GetMapping("/admin/deleted/roles")
    public ResponseEntity<ApiResponseOk> loadOrSearchAdminNotActiveRolesPaged(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") String term
    ){
        PageResponse<RoleDto> roles = roleServices.loadOrSearchAdminNotActiveRolesPaged(page, size, term);
        return ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @GetMapping("/deleted/roles")
    public ResponseEntity<ApiResponseOk> loadOrSearchNotAdminNotActiveRolesPaged(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") String term
    ){
        PageResponse<RoleDto> roles = roleServices.loadOrSearchNotAdminNotActiveRolesPaged(page, size, term);
        return ResponseEntity.ok(new ApiResponseOk("success", roles));
    }


    @DeleteMapping("/admin/roles/{id}")
    public ResponseEntity<ApiResponseOk> deleteRole(@PathVariable UUID id) {
        String message = roleServices.deleteRole(id);
        return ResponseEntity.ok(new ApiResponseOk(message, null));
    }

}
