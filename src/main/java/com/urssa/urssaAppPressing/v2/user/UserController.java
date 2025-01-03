package com.urssa.urssaAppPressing.v2.user;

import com.urssa.urssaAppPressing.v2.appConfig.response.ApiResponseOk;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.user.dto.AddUserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UpdateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/access")
@RequiredArgsConstructor
public class UserController {
    private final UserServicesImpl services;

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponseOk> loadUserById(@PathVariable UUID id) {
        UserDto user = services.loadUserById(id);
        return ResponseEntity.ok(new ApiResponseOk("success", user));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponseOk> addUser(@RequestBody AddUserDto request) {
        UserDto user = services.addUser(request);
        return ResponseEntity.ok(new ApiResponseOk("success", user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponseOk> updateUser(@RequestBody UpdateUserDto request, @PathVariable UUID id) {
        UserDto user = services.updateUser(request, id);
        return ResponseEntity.ok(new ApiResponseOk("success", user));
    }

    @DeleteMapping("/users/{id}")
    public  ResponseEntity<ApiResponseOk> softDeleteUser(@PathVariable UUID id) {
        String message = services.softDeleteUser(id);
        return ResponseEntity.ok(new ApiResponseOk(message, null));
    }

    @DeleteMapping("/users/{id}/delete")
    public  ResponseEntity<ApiResponseOk> deleteUser(@PathVariable UUID id) {
        String message = services.deleteUser(id);
        return ResponseEntity.ok(new ApiResponseOk(message, null));
    }

    @PutMapping("/users/{id}/password")
    public ResponseEntity<ApiResponseOk> updatePassword(@RequestParam(defaultValue = "") String password, @PathVariable UUID id) {
        String message = services.updatePassword(password, id);
        return ResponseEntity.ok(new ApiResponseOk(message, null));
    }

    @PutMapping("/users/{id}/{roleId}/role")
    public ResponseEntity<ApiResponseOk> updateRole(@PathVariable UUID id, @PathVariable UUID roleId) {
        UserDto user = services.updateUserRoles(roleId, id);
        return ResponseEntity.ok(new ApiResponseOk("success", user));
    }

    @GetMapping("/users/all")
    public ResponseEntity<ApiResponseOk> loadOrSearchUsers(
            @RequestParam(defaultValue = "") UUID companyId,
            @RequestParam(defaultValue = "") UUID agencyId,
            @RequestParam(defaultValue = "") String term
    ) {
        List<UserDto> users = services.loadOrSearchActiveUser(companyId, agencyId,term);
        return ResponseEntity.ok( new ApiResponseOk("success", users));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponseOk> loadOrSearchUsersPaged(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") UUID companyId,
            @RequestParam(defaultValue = "") UUID agencyId,
            @RequestParam(defaultValue = "") String term
    ) {
        PageResponse<UserDto> users = services.loadOrSearchActiveUserPaged(page, size, companyId, agencyId,term);
        return ResponseEntity.ok( new ApiResponseOk("success", users));
    }

    @GetMapping("/users/deleted")
    public ResponseEntity<ApiResponseOk> loadOrSearchDeletedUsersPaged(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "") UUID companyId,
            @RequestParam(defaultValue = "") UUID agencyId,
            @RequestParam(defaultValue = "") String term
    ) {
        PageResponse<UserDto> users = services.loadOrSearchDeletedUserPaged(page, size, companyId, agencyId,term);
        return ResponseEntity.ok( new ApiResponseOk("success", users));
    }

}
