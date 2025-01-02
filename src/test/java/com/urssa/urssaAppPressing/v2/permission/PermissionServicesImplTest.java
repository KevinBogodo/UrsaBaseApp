package com.urssa.urssaAppPressing.v2.permission;

import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.permission.dto.AddPermissionDto;
import com.urssa.urssaAppPressing.v2.permission.dto.PermissionDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionServicesImplTest {

    @InjectMocks
    private PermissionServicesImpl permissionServices;

    @Mock
    private PermissionRepository repository;

    @Mock
    private PermissionMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    static void afterAll() {
        System.out.println(" ***************** Permissions Services successfully tested ****************");
    }

    // Load and search by term
    @Test
    public void should_return_all_permissions_when_search_term_is_empty() {
        // Fake data
        UUID mockId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build()
        );
        // Mock the calls
        when(repository.findAll()).thenReturn(permissions);
        when(mapper.convertToDto(any(Permission.class)))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );
        // Action
        List<PermissionDto> permissionDtos = permissionServices.loadOrSearchAllPermission(null);
        // Then
        assertEquals(permissions.size(), permissionDtos.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void should_return_all_permissions_when_search_term_as_value() {
        String term = "Ad";
        UUID mockId = UUID.randomUUID();
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build()
        );
        // Mock the calls
        when(repository.findByNameContainingIgnoreCase(term)).thenReturn(permissions);
        when(mapper.convertToDto(any(Permission.class)))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );

        // Execution
        List<PermissionDto> permissionDtos = permissionServices.loadOrSearchAllPermission(term);
        // Then
        assertEquals(permissions.size(), permissionDtos.size());
        verify(repository, times(1)).findByNameContainingIgnoreCase(term);
    }


    // Load and search by pageable
    @Test
    public void should_return_data_when_all_params_set() {
        // Fake data
        Long page = 0L;
        Long size = 10L;
        String term = "Adm";

        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build();

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());


        Page<Permission> permissionPage = new PageImpl<>(
                List.of(permission),
                pageable,
                1
        );

        // Mock data
         when(repository.findByNameContainingIgnoreCase(term, pageable))
                 .thenReturn(permissionPage);
         when(mapper.convertToDto(permission))
                 .thenReturn(new PermissionDto(
                         mockId,
                         "Admin",
                         "Admin")
                 );

         // Action
        PageResponse<PermissionDto> responseData = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);

        //Then
        assertNotNull(responseData);
        assertEquals(1, responseData.getTotalElements());
        assertEquals(1, responseData.getTotalPages());
        assertEquals(page.intValue(), responseData.getCurrentPage());
        assertEquals(size.intValue(), responseData.getPageSize());
        assertEquals(1, responseData.getContent().size());
        assertEquals("Admin", responseData.getContent().getFirst().getName());
        assertEquals("Admin", responseData.getContent().getFirst().getCode());

    }

    @Test
    public void should_return_data_when_term_is_not_set() {
        // Fake data
        Long page = 0L;
        Long size = 10L;
        String term = "";

        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build();

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        Page<Permission> permissionPage = new PageImpl<>(
                List.of(permission),
                pageable,
                1
        );

        // Mock data
        when(repository.findAll(pageable))
                .thenReturn(permissionPage);
        when(mapper.convertToDto(permission))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );

        // Action
        PageResponse<PermissionDto> responseData = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);

        //Then
        assertNotNull(responseData);
        assertEquals(1, responseData.getTotalElements());
        assertEquals(1, responseData.getTotalPages());
        assertEquals(page.intValue(), responseData.getCurrentPage());
        assertEquals(size.intValue(), responseData.getPageSize());
        assertEquals(1, responseData.getContent().size());
        assertEquals(size.intValue(), responseData.getPageSize());
        assertEquals("Admin", responseData.getContent().getFirst().getName());
        assertEquals("Admin", responseData.getContent().getFirst().getCode());
    }

    @Test
    public void should_return_data_when_page_is_not_set() {
        // Fake data
        Long page = null;
        Long size = 10L;
        String term = "Adm";

        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build();

        Pageable pageable = PageRequest.of(0, size.intValue());

        Page<Permission> permissionPage = new PageImpl<>(
                List.of(permission),
                pageable,
                1
        );

        // Mock data
        when(repository.findByNameContainingIgnoreCase(term, pageable))
                .thenReturn(permissionPage);
        when(mapper.convertToDto(permission))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );

        // Action
        PageResponse<PermissionDto> responseData = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);

        //Then
        assertNotNull(responseData);
        assertEquals(1, responseData.getTotalElements());
        assertEquals(1, responseData.getTotalPages());
        assertEquals(0, responseData.getCurrentPage());
        assertEquals(size.intValue(), responseData.getPageSize());
        assertEquals(1, responseData.getContent().size());
        assertEquals("Admin", responseData.getContent().getFirst().getName());
        assertEquals("Admin", responseData.getContent().getFirst().getCode());

    }

    @Test
    public void should_return_data_when_size_is_not_set() {
        // Fake data
        Long page = 0L;
        Long size = null;
        String term = "Adm";

        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build();

        Pageable pageable = PageRequest.of(page.intValue(), 10);


        Page<Permission> permissionPage = new PageImpl<>(
                List.of(permission),
                pageable,
                1
        );

        // Mock data
        when(repository.findByNameContainingIgnoreCase(term, pageable))
                .thenReturn(permissionPage);
        when(mapper.convertToDto(permission))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );

        // Action
        PageResponse<PermissionDto> responseData = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);

        //Then
        assertNotNull(responseData);
        assertEquals(1, responseData.getTotalElements());
        assertEquals(1, responseData.getTotalPages());
        assertEquals(page.intValue(), responseData.getCurrentPage());
        assertEquals(10, responseData.getPageSize());
        assertEquals(1, responseData.getContent().size());
        assertEquals("Admin", responseData.getContent().getFirst().getName());
        assertEquals("Admin", responseData.getContent().getFirst().getCode());

    }

    @Test
    public void should_return_data_when_page_and_size_not_set() {
        // Fake data
        Long page = null;
        Long size = null;
        String term = "Ad";

        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Permission> permissionPage = new PageImpl<>(
                List.of(permission),
                pageable,
                1
        );

        // Mock data
        when(repository.findByNameContainingIgnoreCase(term, pageable))
                .thenReturn(permissionPage);
        when(mapper.convertToDto(permission))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );

        // Action
        PageResponse<PermissionDto> responseData = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);

        //Then
        assertNotNull(responseData);
        assertEquals(1, responseData.getTotalElements());
        assertEquals(1, responseData.getTotalPages());
        assertEquals(0, responseData.getCurrentPage());
        assertEquals(10, responseData.getPageSize());
        assertEquals(1, responseData.getContent().size());
        assertEquals("Admin", responseData.getContent().getFirst().getName());
        assertEquals("Admin", responseData.getContent().getFirst().getCode());

    }

    @Test
    public void should_return_data_when_term_and_size_are_not_set() {
        // Fake data
        Long page = 0L;
        Long size = null;
        String term = "";

        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build();

        Pageable pageable = PageRequest.of(page.intValue(), 10);

        Page<Permission> permissionPage = new PageImpl<>(
                List.of(permission),
                pageable,
                1
        );

        // Mock data
        when(repository.findAll(pageable)).thenReturn(permissionPage);
        when(mapper.convertToDto(permission))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );

        // Action
        PageResponse<PermissionDto> responseData = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);

        //Then
        assertNotNull(responseData);
        assertEquals(1, responseData.getTotalElements());
        assertEquals(1, responseData.getTotalPages());
        assertEquals(page.intValue(), responseData.getCurrentPage());
        assertEquals(10, responseData.getPageSize());
        assertEquals(1, responseData.getContent().size());
        assertEquals("Admin", responseData.getContent().getFirst().getName());
        assertEquals("Admin", responseData.getContent().getFirst().getCode());
    }

    @Test
    public void should_return_data_when_term_and_page_are_not_set() {
        // Fake data
        Long page = null;
        Long size = 10L;
        String term = "";

        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build();

        Pageable pageable = PageRequest.of(0, size.intValue());

        Page<Permission> permissionPage = new PageImpl<>(
                List.of(permission),
                pageable,
                1
        );

        // Mock data
        when(repository.findAll(pageable))
                .thenReturn(permissionPage);
        when(mapper.convertToDto(permission))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );

        // Action
        PageResponse<PermissionDto> responseData = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);

        //Then
        assertNotNull(responseData);
        assertEquals(1, responseData.getTotalElements());
        assertEquals(1, responseData.getTotalPages());
        assertEquals(0, responseData.getCurrentPage());
        assertEquals(size.intValue(), responseData.getPageSize());
        assertEquals(1, responseData.getContent().size());
        assertEquals("Admin", responseData.getContent().getFirst().getName());
        assertEquals("Admin", responseData.getContent().getFirst().getCode());
    }

    @Test
    public void should_return_data_when_params_not_set() {
        // Fake data
        Long page = null;
        Long size = null;
        String term = "";

        UUID mockId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(mockId)
                .name("Admin")
                .code("Admin")
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Permission> permissionPage = new PageImpl<>(
                List.of(permission),
                pageable,
                1
        );

        // Mock data
        when(repository.findAll(pageable))
                .thenReturn(permissionPage);
        when(mapper.convertToDto(permission))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );

        // Action
        PageResponse<PermissionDto> responseData = permissionServices.loadOrSearchAllPermissionPaginated(page, size, term);

        //Then
        assertNotNull(responseData);
        assertEquals(1, responseData.getTotalElements());
        assertEquals(1, responseData.getTotalPages());
        assertEquals(0, responseData.getCurrentPage());
        assertEquals(10, responseData.getPageSize());
        assertEquals(1, responseData.getContent().size());
        assertEquals("Admin", responseData.getContent().getFirst().getName());
        assertEquals("Admin", responseData.getContent().getFirst().getCode());
    }



    // Load by Id
    @Test
    public void should_return_permission_by_id() {
        // Fake data
        UUID permissionId = UUID.randomUUID();

        Permission permission = Permission.builder()
                .id(permissionId)
                .name("Admin")
                .code("Admin")
                .build();
        // Mock the calls
        when(repository.findById(permissionId))
                .thenReturn(Optional.of(permission));

        when(mapper.convertToDto(any(Permission.class)))
                .thenReturn(new PermissionDto(
                        permissionId,
                        "Admin",
                        "Admin")
                );

        // Action
        PermissionDto dto = permissionServices.loadPermissionById(permissionId);

        // Then
        assertEquals(permission.getId(), dto.getId());
        assertEquals(permission.getName(), dto.getName());
        assertEquals(permission.getCode(), dto.getCode());

        verify(repository, times(1)).findById(permissionId);
    }

    @Test
    public void should_fail_when_permission_id_is_null() {
        // Action
        var exp = assertThrows(NullPointerException.class, ()->permissionServices.loadPermissionById(null));
        // Then
        assertEquals("id_cannot_be_null", exp.getMessage());
    }



    // Add pemission
    @Test
    public void should_successfully_save_a_permission() {

    UUID mockId = UUID.randomUUID();

        AddPermissionDto dto = new AddPermissionDto(
                "Admin",
                "Admin"
        );

        Permission permission = Permission.builder()
                .name("Admin")
                .code("Admin")
                .build();

        Permission savedPermission = Permission.builder()
            .id(mockId)
            .name("Admin")
            .code("Admin")
            .build();

        // Mock the calls
        when(mapper.convertToPermission(dto))
                .thenReturn(permission);
        when(repository.save(permission)).thenReturn(savedPermission);
        when(mapper.convertToDto(savedPermission))
                .thenReturn(new PermissionDto(
                        mockId,
                        "Admin",
                        "Admin")
                );


        PermissionDto permissionDto = permissionServices.addPermission(dto);

        assertEquals(dto.getCode(), permissionDto.getCode());
        assertEquals(dto.getName(), permissionDto.getName());
        assertEquals(mockId, permissionDto.getId());
        assertNotNull(permissionDto.getId());

        verify(mapper, times(1))
                .convertToPermission(dto);
        verify(repository, times(1))
                .save(permission);
        verify(mapper, times(1))
                .convertToDto(savedPermission);
    }

    @Test
    public void should_fail_save_when_name_is_null() {

        AddPermissionDto dto = new AddPermissionDto(
            "",
            "Admin"
        );

        var exp = assertThrows(IllegalArgumentException.class, ()->permissionServices.addPermission(dto));
        assertEquals("name_cannot_be_empty", exp.getMessage());

    }

    @Test
    public void should_fail_save_when_code_is_null() {

        AddPermissionDto dto = new AddPermissionDto(
                "Admin",
                ""
        );

        var exp = assertThrows(IllegalArgumentException.class, ()->permissionServices.addPermission(dto));
        assertEquals("code_cannot_be_empty", exp.getMessage());

    }

    @Test
    public void should_fail_save_when_name_is_used() {

        AddPermissionDto dto = new AddPermissionDto(
                "Admin",
                "Admin"
        );
//        Mock
        when(repository.existsByName("Admin")).thenReturn(true);
//        Action
        var exp = assertThrows(IllegalArgumentException.class, ()->permissionServices.addPermission(dto));
//        Then
        assertEquals("name_already_exists", exp.getMessage());
    }

    @Test
    public void should_fail_save_when_code_is_used() {

        AddPermissionDto dto = new AddPermissionDto(
                "Admin",
                "Admin"
        );
//        Mock
        when(repository.existsByCode("Admin")).thenReturn(true);
//        Action
        var exp = assertThrows(IllegalArgumentException.class, ()->permissionServices.addPermission(dto));
//        Then
        assertEquals("code_already_exists", exp.getMessage());
    }



    // Update permission
    @Test
    public void should_successfully_update_a_permission() {

        UUID permissionId = UUID.randomUUID();

        AddPermissionDto dto = new AddPermissionDto(
                "Admin",
                "Admin"
        );

        Permission permission = Permission.builder()
                .name("Admin")
                .code("Admin")
                .build();

        Permission savedPermission = Permission.builder()
                .id(permissionId)
                .name("Admin")
                .code("Admin")
                .build();

        // Mock the calls
        when(repository.findById(permissionId))
                .thenReturn(Optional.of(permission));
        when(repository.save(any(Permission.class)))
                .thenReturn(savedPermission);
        when(mapper.convertToDto(savedPermission))
                .thenReturn(new PermissionDto(
                        permissionId,
                        "Admin",
                        "Admin")
                );

        //Action
        PermissionDto permissionDto = permissionServices.updatePermission(dto, permissionId);
        // Then
        assertEquals(dto.getCode(), permissionDto.getCode());
        assertEquals(dto.getName(), permissionDto.getName());
        assertEquals(permissionId, permissionDto.getId());
        assertNotNull(permissionDto.getId());

        verify(repository, times(1)).save(permission);
        verify(repository, times(1)).findById(permissionId);
        verify(mapper, times(1)).convertToDto(savedPermission);
    }

    @Test
    public void should_fail_update_when_id_is_undefined() {

        AddPermissionDto dto = new AddPermissionDto(
                "Admin",
                "Admin"
        );

        var exp = assertThrows(NullPointerException.class, ()->permissionServices.updatePermission(dto, null));
        assertEquals("id_cannot_be_null", exp.getMessage());

    }

    @Test
    public void should_fail_update_when_name_is_null() {
        UUID permissionId = UUID.randomUUID();
        AddPermissionDto dto = new AddPermissionDto(
                "",
                "Admin"
        );

        var exp = assertThrows(IllegalArgumentException.class, ()->permissionServices.updatePermission(dto, permissionId));
        assertEquals("name_cannot_be_empty", exp.getMessage());

    }

    @Test
    public void should_fail_update_when_code_is_null() {
        UUID permissionId = UUID.randomUUID();
        AddPermissionDto dto = new AddPermissionDto(
                "Admin",
                ""
        );

        var exp = assertThrows(IllegalArgumentException.class, ()->permissionServices.updatePermission(dto, permissionId));
        assertEquals("code_cannot_be_empty", exp.getMessage());

    }

    @Test
    public void should_fail_update_when_update_data_is_null() {
        UUID permissionId = UUID.randomUUID();
        AddPermissionDto dto = null;

        var exp = assertThrows(NullPointerException.class, ()->permissionServices.updatePermission(dto, permissionId));
        assertEquals("update_data_object_cannot_be_empty", exp.getMessage());

    }



}