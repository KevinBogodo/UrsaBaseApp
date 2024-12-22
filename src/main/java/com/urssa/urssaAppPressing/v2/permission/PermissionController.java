package com.urssa.urssaAppPressing.v2.permission;


import com.urssa.urssaAppPressing.v2.appConfig.response.ApiResponseOk;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.permission.dto.AddPermissionDto;
import com.urssa.urssaAppPressing.v2.permission.dto.PermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/access")
public class PermissionController {

    private final PermissionServicesImpl permissionServices;

    @GetMapping("/permissions/search")
    public ResponseEntity<ApiResponseOk> loadOrSearchPermissions(
            @RequestParam(defaultValue = "") String term
    ) {
            List<PermissionDto> allPermissions = permissionServices.loadOrSearchAllPermission(term);
            return ResponseEntity.ok(new ApiResponseOk("success", allPermissions));
    }

    @GetMapping("/permissions")
    public ResponseEntity<ApiResponseOk>loadOrSearchAllPermissionsPaginated(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") String term
    ) {
        PageResponse<PermissionDto> allPermissions = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);
        return ResponseEntity.ok(new ApiResponseOk("success", allPermissions));
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<ApiResponseOk> loadPermissionByName(
            @PathVariable UUID id
    ) {
        PermissionDto permission = permissionServices.loadPermissionById(id);
        return  ResponseEntity.ok(new ApiResponseOk("success", permission));
    }

    @PostMapping("/permissions")
    public ResponseEntity<ApiResponseOk> createPermission(
            @Validated @RequestBody AddPermissionDto request
    ) {
        PermissionDto permission = permissionServices.addPermission(request);
        return ResponseEntity.ok(new ApiResponseOk("success", permission));
    }

    @PutMapping("/permissions/{id}")
    public ResponseEntity<ApiResponseOk> updatePermission(
            @Validated @RequestBody AddPermissionDto request,
            @PathVariable UUID id
    ) {
        PermissionDto permission = permissionServices.updatePermission(request, id);
        return ResponseEntity.ok(new ApiResponseOk("success", permission));
    }
}
