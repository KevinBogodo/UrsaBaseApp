package com.urssa.urssaAppPressing.v2.role;


import com.urssa.urssaAppPressing.v2.appConfig.response.ApiResponseOk;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.role.dto.AddRoleDto;
import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/access")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServices roleServices;

    @GetMapping("/roles/search")
    public ResponseEntity<ApiResponseOk> loadOrSearchActiveRoles(@RequestParam String term) {

        List<RoleDto> roles = roleServices.loadOrSearchActiveRoles(term);
        return  ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponseOk> loadOrSearchActiveRolesPaged(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") String term
    ){
        PageResponse<RoleDto> roles = roleServices.loadOrSearchRolesPaged(page, size, term);
        return ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<ApiResponseOk> loadRoleById(@PathVariable UUID id) {
        RoleDto role = roleServices.loadRoleById(id);
        return ResponseEntity.ok(new ApiResponseOk("success", role));
    }

    @PostMapping("/roles")
    public ResponseEntity<ApiResponseOk> createRole (@Validated @RequestBody AddRoleDto request) {
        RoleDto role = roleServices.addRoles(request);
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

    @GetMapping("/admin/roles/search")
    public ResponseEntity<ApiResponseOk> loadOrSearchRoles(@RequestParam String term) {

        List<RoleDto> roles = roleServices.loadOrSearchRoles(term);
        return  ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @GetMapping("/admin/roles")
    public ResponseEntity<ApiResponseOk> loadAndSearchRolesPaged(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") String term
    ){
        PageResponse<RoleDto> roles = roleServices.loadOrSearchRolesPaged(page, size, term);
        return ResponseEntity.ok(new ApiResponseOk("success", roles));
    }

    @DeleteMapping("/admin/roles/{id}")
    public ResponseEntity<ApiResponseOk> deleteRole(@PathVariable UUID id) {
        String message = roleServices.deleteRole(id);
        return ResponseEntity.ok(new ApiResponseOk(message, null));
    }

}
