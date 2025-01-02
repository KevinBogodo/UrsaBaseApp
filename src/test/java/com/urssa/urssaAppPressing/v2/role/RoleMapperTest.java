package com.urssa.urssaAppPressing.v2.role;

import com.urssa.urssaAppPressing.v2.permission.Permission;
import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperTest {

    private RoleMapper mapper;
    @BeforeEach
    void setUp() {
        mapper = new RoleMapper();
    }

    @AfterAll
    static void afterAll() {
        System.out.println("*********************RoleMapper tested ok ************************");
    }


    @Test
    public void should_fail_when_id_is_null() {
        Role role = Role.builder()
                        .id(null)
                        .name("Admin")
                        .build();
        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToRoleDto(role));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_when_name_is_blank() {
        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("")
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToRoleDto(role));
        assertEquals("name_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_fail_when_dto_is_null(){
        var exp = assertThrows(NullPointerException.class, ()->mapper.convertToRoleDto(null));
        assertEquals("the_role_is_null", exp.getMessage());
    }

    @Test
    public void should_convert_role_to_role_dto() {
        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .code("Admin")
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        Set<Permission> permissions = new HashSet<>();
            permissions.add(permission);

        Role role = Role.builder()
                .id(mockId)
                .name("Admin")
                .permissions(permissions)
                .build();

        RoleDto roleDto = new RoleDto(
                mockId,
                "Admin",
                false,
                permissions
        );

        RoleDto result = mapper.convertToRoleDto(role);

        System.out.println("result: "+result);
        assertNotNull(result, "result_cannot_be_null_in_conversion");
        assertEquals(role.getId(), result.getId());
        assertEquals(role.getName(), result.getName());
        assertEquals(role.getPermissions(), result.getPermissions());

    }

    @Test
    public void should_convert_role_to_role_dto_when_permission_not_set() {
        UUID mockId = UUID.randomUUID();

        Role role = Role.builder()
                .id(mockId)
                .name("Admin")
                .build();


        RoleDto result = mapper.convertToRoleDto(role);
        assertNotNull(result, "result_cannot_be_null_in_conversion");
        assertEquals(role.getId(), result.getId());
        assertEquals(role.getName(), result.getName());
        assertEquals(role.getPermissions(), result.getPermissions());
    }

    @Test
    public void should_convert_role_to_role_dto_when_permissions_is_empty() {
        UUID mockId = UUID.randomUUID();


        Set<Permission> permissions = new HashSet<>();

        Role role = Role.builder()
                .id(mockId)
                .name("Admin")
                .permissions(permissions)
                .build();

        RoleDto result = mapper.convertToRoleDto(role);
        assertNotNull(result, "result_cannot_be_null_in_conversion");
        assertEquals(role.getId(), result.getId());
        assertEquals(role.getName(), result.getName());
        assertEquals(role.getPermissions(), result.getPermissions());

    }

    @Test
    public void should_convert_role_to_role_dto_when_permissions_is_null() {
        UUID mockId = UUID.randomUUID();

        Role role = Role.builder()
                .id(mockId)
                .name("Admin")
                .permissions(null)
                .build();

        RoleDto result = mapper.convertToRoleDto(role);
        assertNotNull(result, "result_cannot_be_null_in_conversion");
        assertEquals(role.getId(), result.getId());
        assertEquals(role.getName(), result.getName());
        assertEquals(role.getPermissions(), result.getPermissions());

    }

}