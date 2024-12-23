package com.urssa.urssaAppPressing.v2.permission;

import com.urssa.urssaAppPressing.v2.permission.dto.AddPermissionDto;
import com.urssa.urssaAppPressing.v2.permission.dto.PermissionDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PermissionMapperTest {

    private PermissionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PermissionMapper();
    }

    @AfterAll
    static void afterAll() {
        System.out.println(" ******************** PermissionMapper tested ok *************************");
    }


    // convert to DTO
    @Test
    public void should_fail_when_id_is_null() {
        Permission permission = Permission.builder()
                .id(null)
                .name("admin")
                .code("admin")
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToDto(permission));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }


    @Test
    public void should_fail_when_code_is_blank() {
        Permission permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("admin")
                .code(null)
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToDto(permission));
        assertEquals("code_cannot_be_empty", exp.getMessage());
    }


    @Test
    public void should_fail_when_name_is_blank() {
        Permission permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("")
                .code("admin")
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToDto(permission));
        assertEquals("name_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_fail_when_created_by_is_null() {
        Permission permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("name")
                .code("admin")
                .createdBy(null)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToDto(permission));
        assertEquals("created_by_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_when_created_at_is_null() {
        Permission permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("admin")
                .code("admin")
                .createdBy(UUID.randomUUID())
                .createdAt(null)
                .updatedAt(LocalDateTime.now())
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToDto(permission));
        assertEquals("created_at_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_when_updated_at_is_null() {
        Permission permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("admin")
                .code("admin")
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(null)
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToDto(permission));
        assertEquals("updated_at_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_convert_permission_to_permissionDto() {
        Permission permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("admin")
                .code("admin")
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();
        PermissionDto result = mapper.convertToDto(permission);

        assertNotNull(result, "Result cannot be null");
        assertEquals(permission.getId(), result.getId());
        assertEquals(permission.getName(), result.getName());
        assertEquals(permission.getCode(), result.getCode());
        assertEquals(permission.getCreatedBy(), result.getCreatedBy());
        assertEquals(permission.getCreatedAt(), result.getCreatedAt());
        assertEquals(permission.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    public void should_throw_null_pointer_exception_when_addPermissionDto_is_nul() {
        var exp = assertThrows(NullPointerException.class, () -> mapper.convertToDto(null));
        assertNotNull(exp);
        assertEquals("The permission is null", exp.getMessage());
    }




    // Convert Dto to permission
    @Test
    public void should_fail_when_add_permissionDto_name_is_blank() {
        AddPermissionDto dto = new AddPermissionDto(
                "",
                "Admin"
        );

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToPermission(dto));
        assertEquals("name_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_fail_when_add_permissionDto_code_is_blank() {
        AddPermissionDto dto = new AddPermissionDto(
                "Admin",
                ""
        );

        var exp = assertThrows(IllegalArgumentException.class, ()->mapper.convertToPermission(dto));
        assertEquals("code_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_convert_permissionDto_to_permission() {

        AddPermissionDto dto = new AddPermissionDto(
            "Admin",
            "Admin"
        );

        Permission result = mapper.convertToPermission(dto);

        assertNotNull(result, "Result cannot be null");
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getCode(), result.getCode());
    }

    @Test
    public void should_throw_null_pointer_exception_when_addPermissionDto_is_null() {
        var exp = assertThrows(NullPointerException.class, () -> mapper.convertToPermission(null));
        assertNotNull(exp);
        assertEquals("The permissionDto is null", exp.getMessage());
    }
}