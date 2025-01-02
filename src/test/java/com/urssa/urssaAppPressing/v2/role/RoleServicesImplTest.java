package com.urssa.urssaAppPressing.v2.role;

import com.urssa.urssaAppPressing.v2.appConfig.execption.RessourcesNotFoundException;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.permission.Permission;
import com.urssa.urssaAppPressing.v2.permission.PermissionRepository;
import com.urssa.urssaAppPressing.v2.permission.dto.AddPermissionDto;
import com.urssa.urssaAppPressing.v2.role.dto.AddRoleDto;
import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServicesImplTest {

    @InjectMocks
    private RoleServicesImpl roleServices;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    static void afterAll() {
        System.out.println("************ Roles tested *********");
    }


//     Load by id
    @Test
    public void should_return_role_by_id() {
//        Fake data
        UUID roleId = UUID.randomUUID();
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .code("Admin")
                .build());

        Role role = Role.builder()
                .id(roleId)
                .name("Admin")
                .permissions(permissions)
                .build();

        RoleDto roleDto = new RoleDto(
                roleId,
                "Admin",
                false,
                permissions
        );

//        Mock Action
        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));
        when(mapper.convertToRoleDto(role)).thenReturn(roleDto);

//        Action
        RoleDto result = roleServices.loadRoleById(roleId);

//        Then
        assertNotNull(result);
        assertEquals(roleId, result.getId());
        assertEquals(role.getName(), result.getName());
        assertEquals(role.getPermissions(), result.getPermissions());
        verify(roleRepository,times(1)).findById(roleId);
        verify(mapper, times(1)).convertToRoleDto(role);
    }

    @Test
    public void Should_fail_when_role_id_is_null() {
        var exp = assertThrows(NullPointerException.class, ()->roleServices.loadRoleById(null));
        assertEquals("id_cannot_be_null_to_search", exp.getMessage());
    }

    @Test
    public void Should_fail_when_role_not_found() {
        var exp = assertThrows(RessourcesNotFoundException.class, ()->roleServices.loadRoleById(UUID.randomUUID()));
        assertEquals("role_not_found", exp.getMessage());
    }


//     Add Role
    @Test
    public void should_successfully_save_a_role() {
//        Fake data
        UUID permissionId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        Set<UUID> permissionsIdList = new HashSet<>();
        permissionsIdList.add(permissionId);

        String type = "client";
        AddRoleDto request = new AddRoleDto(
                "Admin",
                permissionsIdList
        );

        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.builder()
                .id(permissionId)
                .name("Admin")
                .code("Admin")
                .build());


        Role role = Role.builder()
                .name("Admin")
                .permissions(permissions)
                .build();

        Role savedRole = Role.builder()
                .id(roleId)
                .name("Admin")
                .permissions(permissions)
                .build();

        RoleDto roleDto = new RoleDto(
                roleId,
                "Admin",
                false,
                permissions
        );

//        Mock calls
        when(permissionRepository.findAllById(permissionsIdList)).thenReturn(new ArrayList<>(permissions));
        when(roleRepository.save(role)).thenReturn(savedRole);
        when(mapper.convertToRoleDto(savedRole)).thenReturn(roleDto);

//        Action
        RoleDto result = roleServices.addRoles(request, type);

//        Then
        assertEquals(request.getName(), result.getName());
        assertEquals(savedRole.getId(), result.getId());
        assertEquals(request.getPermissions().stream().findFirst().get(), result.getPermissions().stream().findFirst().get().getId());
    }

    @Test
    public void should_successfully_save_a_role_when_permissions_is_empty() {
//        Fake data
        UUID roleId = UUID.randomUUID();

        String type = "sadwe";
        AddRoleDto request = new AddRoleDto(
                "Admin",
                null
        );

        Role role = Role.builder()
                .name("Admin")
                .build();

        Role savedRole = Role.builder()
                .id(roleId)
                .name("Admin")
                .build();

        RoleDto roleDto = new RoleDto(
                roleId,
                "Admin",
                false,
                null
        );

//        Mock calls
        when(roleRepository.save(role)).thenReturn(savedRole);
        when(mapper.convertToRoleDto(savedRole)).thenReturn(roleDto);

//        Action
        RoleDto result = roleServices.addRoles(request, type);

//        Then
        assertEquals(request.getName(), result.getName());
        assertEquals(savedRole.getId(), result.getId());
    }

    @Test
    public void should_successfully_save_a_role_when_type_is_not_empty() {
//        Fake data
        UUID permissionId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        Set<UUID> permissionsIdList = new HashSet<>();
        permissionsIdList.add(permissionId);

        String type = "admin";
        AddRoleDto request = new AddRoleDto(
                "Admin",
                permissionsIdList
        );

        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.builder()
                .id(permissionId)
                .name("Admin")
                .code("Admin")
                .build());



        Role role = Role.builder()
                .name("Admin")
                .permissions(permissions)
                .isAdmin(true)
                .build();

        Role savedRole = Role.builder()
                .id(roleId)
                .name("Admin")
                .isAdmin(true)
                .permissions(permissions)
                .build();

        RoleDto roleDto = new RoleDto(
                roleId,
                "Admin",
                true,
                permissions
        );

//        Mock calls
        when(permissionRepository.findAllById(permissionsIdList)).thenReturn(new ArrayList<>(permissions));
        when(roleRepository.save(role)).thenReturn(savedRole);
        when(mapper.convertToRoleDto(savedRole)).thenReturn(roleDto);

//        Action
        RoleDto result = roleServices.addRoles(request, type);

//        Then
        assertEquals(request.getName(), result.getName());
        assertEquals(savedRole.getId(), result.getId());
        assertEquals(request.getPermissions().stream().findFirst().get(), result.getPermissions().stream().findFirst().get().getId());
    }

    @Test
    public void should_fail_save_when_name_is_blank(){
        Set<UUID> permissionsIdList = new HashSet<>();
        permissionsIdList.add(UUID.randomUUID());
        String type = "admin";
        AddRoleDto request = new AddRoleDto(
                "",
                permissionsIdList
        );

        var exp = assertThrows(IllegalArgumentException.class, ()->roleServices.addRoles(request, type));
        assertEquals("name_cannot_be_blank", exp.getMessage());
    }

    @Test
    public void should_fail_save_when_name_is_used() {

        AddRoleDto dto = new AddRoleDto(
                "Admin",
                null
        );
//        Mock
        when(roleRepository.existsByName("Admin")).thenReturn(true);
//        Action
        var exp = assertThrows(IllegalArgumentException.class, ()->roleServices.addRoles(dto, ""));
//        Then
        assertEquals("name_already_exists", exp.getMessage());
    }


    // Update Role

    @Test
    public void should_fail_update_when_id_is_undefined() {

        AddRoleDto dto = new AddRoleDto(
                "Admin",
                null
        );

        var exp = assertThrows(NullPointerException.class, () -> roleServices.updateRole(dto, null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_update_when_name_is_null() {
        AddRoleDto dto = new AddRoleDto(
                "",
                null
        );

        var exp =  assertThrows(IllegalArgumentException.class, () -> roleServices.updateRole(dto, UUID.randomUUID()));
        assertEquals("name_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_fail_update_when_role_not_found() {
        AddRoleDto dto = new AddRoleDto(
                "Admin",
                null
        );

        Role role = null;

        var exp = assertThrows(RessourcesNotFoundException.class, () -> roleServices.updateRole(dto, UUID.randomUUID()));
        assertEquals("role_not_found", exp.getMessage());
    }

    @Test
    public void should_fail_update_when_data_is_null(){

        var exp = assertThrows(IllegalArgumentException.class, () -> roleServices.updateRole(null, UUID.randomUUID()));
        assertEquals("request_data_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_successfully_update_a_role() {

        UUID roleId =  UUID.randomUUID();
        UUID permissionId = UUID.randomUUID();

        Set<UUID> permissionRequest = new HashSet<>();
        permissionRequest.add(permissionId);

        AddRoleDto request = new AddRoleDto(
                "Admin",
                permissionRequest
        );

        Permission permission = Permission.builder()
                .id(permissionId)
                .name("admin")
                .build();

        Set<Permission> permissionsList = new HashSet<>();
        permissionsList.add(permission);


        Role role = Role.builder()
                .id(roleId)
                .name("Admin")
                .isAdmin(false)
                .permissions(permissionsList)
                .build();

        Role savedRole = Role.builder()
                .id(roleId)
                .name("Admin")
                .permissions(permissionsList)
                .isAdmin(false)
                .build();

        RoleDto roledto = new RoleDto(
                roleId,
                "Admin",
                false,
                permissionsList
        );

//        mock
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(savedRole);
        when(mapper.convertToRoleDto(savedRole)).thenReturn(roledto);

//        Action
        RoleDto  savedDto = roleServices.updateRole(request, roleId);
//        Then
        assertNotNull(savedDto);
        assertEquals(roleId, savedDto.getId());
        assertEquals(request.getName(), savedDto.getName());
        assertEquals(request.getPermissions().size(), savedDto.getPermissions().size());

    }



    // Delete Role

    @Test
    public void should_successfully_set_deleted_role() {
        UUID roleId = UUID.randomUUID();

        Role role = Role.builder()
                .id(roleId)
                .name("Admin")
                .isDeleted(true)
                .build();

//        Mock
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
//        Action
        String response = roleServices.softDeleteRole(roleId);

//        Then
        assertNotNull(response);
        assertEquals("delete_success", response);
    }

    @Test
    public void should_fail_set_deleted_when_id_is_undefined() {
        var exp = assertThrows(NullPointerException.class, () -> roleServices.softDeleteRole(null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_set_delete_when_role_not_found(){
        var exp = assertThrows(RessourcesNotFoundException.class, () -> roleServices.softDeleteRole(UUID.randomUUID()));
        assertEquals("role_not_found", exp.getMessage());
    }

    @Test
    public void should_fail_delete_when_id_is_undefined() {
        var exp = assertThrows(NullPointerException.class, () -> roleServices.deleteRole(null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_delete_when_role_not_found() {
        var exp = assertThrows(RessourcesNotFoundException.class, () -> roleServices.deleteRole(UUID.randomUUID()));
        assertEquals("role_not_found", exp.getMessage());
    }

    @Test
    public void should_successfully_delete() {
        UUID roleId = UUID.randomUUID();

        Role role = Role.builder()
                .id(roleId)
                .name("Admin")
                .isDeleted(true)
                .build();

//        Mock
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
//        when(roleRepository.deleteById(roleId));
//        Action
        String response = roleServices.softDeleteRole(roleId);

//        Then
        assertNotNull(response);
        assertEquals("delete_success", response);
    }



    // Restore Role

    @Test
    public void should_fail_restore_when_id_is_undefined() {
        var exp = assertThrows(NullPointerException.class, () -> roleServices.restoreRole(null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_restore_when_role_not_found() {
        var exp = assertThrows(RessourcesNotFoundException.class, ()-> roleServices.restoreRole(UUID.randomUUID()));
        assertEquals("role_not_found", exp.getMessage());
    }

    @Test
    public void should_successfully_restore_role() {
        UUID roleId = UUID.randomUUID();

        Role role = Role.builder()
                .id(roleId)
                .name("Admin")
                .isDeleted(true)
                .build();

        Role restoredRole = Role.builder()
                .id(roleId)
                .name("Admin")
                .isDeleted(false)
                .build();

        RoleDto dto = new RoleDto(
                roleId,
                "Admin",
                false,
                null
        );

//        Mock
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(restoredRole);
        when(mapper.convertToRoleDto(restoredRole)).thenReturn(dto);
//        Action
        RoleDto responseDto =  roleServices.restoreRole(roleId);

        assertNotNull(responseDto);
        assertEquals(roleId, responseDto.getId());
        assertFalse(restoredRole.isDeleted());
    }



    // change role type

    @Test
    public void should_fail_change_when_id_is_undefined() {
        var exp = assertThrows(NullPointerException.class, () -> roleServices.changeRoleType(null, "admin"));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_change_when_type_is_blank() {
        var exp = assertThrows(IllegalArgumentException.class, () -> roleServices.changeRoleType(UUID.randomUUID(), ""));
        assertEquals("type_cannot_be_undefined", exp.getMessage());
    }

    @Test
    public void should_fail_change_when_role_not_found() {
        var exp = assertThrows(RessourcesNotFoundException.class, () -> roleServices.changeRoleType(UUID.randomUUID(), "admin"));
        assertEquals("role_not_found", exp.getMessage());
    }

    @Test
    public void should_successfully_change_status() {
        UUID roleId = UUID.randomUUID();
        String type = "admin";

        Role role = Role.builder()
                .id(roleId)
                .name("Admin")
                .isAdmin(true)
                .build();

        Role updatedRole = Role.builder()
                .id(roleId)
                .name("Admin")
                .isAdmin(true)
                .build();

        RoleDto dto = new RoleDto(
                roleId,
                "Admin",
                true,
                null
        );
//        Mock
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(updatedRole);
        when(mapper.convertToRoleDto(updatedRole)).thenReturn(dto);
//        Action
        RoleDto responseDto = roleServices.changeRoleType(roleId, type);
//        Then
        assertNotNull(responseDto);
        assertEquals(roleId, responseDto.getId());
        assertTrue(responseDto.getIsAdmin());
    }



    // load or search active normal Roles

    // Not pageable

    @Test
    public void should_return_active_normal_role_when_term_set(){

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse("A")).thenReturn(roleList);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        List<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRoles("A");
        assertNotNull(response);
        assertEquals(roleList.size(), response.size());
        verify(roleRepository, times(1))
                .findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse("A");
    }

    @Test
    public void should_return_active_normal_when_term_is_not_set(){
        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

//        Mock
        when(roleRepository.findByIsDeletedFalseAndIsAdminFalse()).thenReturn(roleList);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        List<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRoles(null);
        assertNotNull(response);
        assertEquals(roleList.size(), response.size());
        verify(roleRepository, times(1)).findByIsDeletedFalseAndIsAdminFalse();

    }

    // Pageable

    @Test
    public void should_return_active_normal_role_when_page_is_not_set(){
        Long page = null;
        Long size = 10L;
        String term = "A";
        Pageable pageable = PageRequest.of( 0, size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);
    }

    @Test
    public void should_return_active_normal_role_when_size_is_not_set(){
        Long page = 0L;
        Long size = null;
        String term = "A";
        Pageable pageable = PageRequest.of( page.intValue(), 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);

    }

    @Test
    public void should_return_active_normal_role_when_page_and_size_not_set(){
        Long page = null;
        Long size = null;
        String term = "A";
        Pageable pageable = PageRequest.of( 0, 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(rolePage.getTotalElements(), response.getTotalElements());
        verify(roleRepository, times(1)).findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);
    }

    @Test
    public void should_return_active_normal_role_when_term_and_page_are_not_set(){
        Long page = null;
        Long size = 10L;
        String term = "";
        Pageable pageable = PageRequest.of( 0, size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedFalseAndIsAdminFalse(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedFalseAndIsAdminFalse(pageable);
    }

    @Test
    public void should_return_active_normal_role_when_term_and_size_are_not_set(){
        Long page = 0L;
        Long size = null;
        String term = "";
        Pageable pageable = PageRequest.of( page.intValue(), 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedFalseAndIsAdminFalse(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedFalseAndIsAdminFalse(pageable);

    }

    @Test
    public void should_return_active_normal_role_when_term_are_not_set(){
        Long page = 0L;
        Long size = 10L;
        String term = "";
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedFalseAndIsAdminFalse(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedFalseAndIsAdminFalse(pageable);
    }

    @Test
    public void should_return_active_normal_role_when_params_set(){
        Long page = 0L;
        Long size = 10L;
        String term = "A";
        Pageable pageable = PageRequest.of( page.intValue(), size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);

    }



    // load or search active admin Roles

    // Not pageable

    @Test
    public void should_return_active_admin_role_when_term_set(){

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue("A")).thenReturn(roleList);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        List<RoleDto> response = roleServices.loadOrSearchAdminActiveRoles("A");
        assertNotNull(response);
        assertEquals(roleList.size(), response.size());
        verify(roleRepository, times(1))
                .findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue("A");
    }

    @Test
    public void should_return_active_admin_when_term_is_not_set(){
        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

//        Mock
        when(roleRepository.findByIsDeletedFalseAndIsAdminTrue()).thenReturn(roleList);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        List<RoleDto> response = roleServices.loadOrSearchAdminActiveRoles(null);
        assertNotNull(response);
        assertEquals(roleList.size(), response.size());
        verify(roleRepository, times(1)).findByIsDeletedFalseAndIsAdminTrue();

    }

    // Pageable

    @Test
    public void should_return_active_admin_role_when_page_is_not_set(){
        Long page = null;
        Long size = 10L;
        String term = "A";
        Pageable pageable = PageRequest.of( 0, size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);
    }

    @Test
    public void should_return_active_admin_role_when_size_is_not_set(){
        Long page = 0L;
        Long size = null;
        String term = "A";
        Pageable pageable = PageRequest.of( page.intValue(), 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);

    }

    @Test
    public void should_return_active_admin_role_when_page_and_size_not_set(){
        Long page = null;
        Long size = null;
        String term = "A";
        Pageable pageable = PageRequest.of( 0, 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(rolePage.getTotalElements(), response.getTotalElements());
        verify(roleRepository, times(1)).findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);
    }

    @Test
    public void should_return_active_admin_role_when_term_and_page_are_not_set(){
        Long page = null;
        Long size = 10L;
        String term = "";
        Pageable pageable = PageRequest.of( 0, size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedFalseAndIsAdminTrue(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedFalseAndIsAdminTrue(pageable);
    }

    @Test
    public void should_return_active_admin_role_when_term_and_size_are_not_set(){
        Long page = 0L;
        Long size = null;
        String term = "";
        Pageable pageable = PageRequest.of( page.intValue(), 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedFalseAndIsAdminTrue(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedFalseAndIsAdminTrue(pageable);

    }

    @Test
    public void should_return_active_admin_role_when_term_are_not_set(){
        Long page = 0L;
        Long size = 10L;
        String term = "";
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedFalseAndIsAdminTrue(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedFalseAndIsAdminTrue(pageable);
    }

    @Test
    public void should_return_active_admin_role_when_params_set(){
        Long page = 0L;
        Long size = 10L;
        String term = "A";
        Pageable pageable = PageRequest.of( page.intValue(), size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);

    }


    // load or search deleted admin Roles

    @Test
    public void should_return_deleted_admin_role_when_page_is_not_set(){
        Long page = null;
        Long size = 10L;
        String term = "A";
        Pageable pageable = PageRequest.of( 0, size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);
    }

    @Test
    public void should_return_deleted_admin_role_when_size_is_not_set(){
        Long page = 0L;
        Long size = null;
        String term = "A";
        Pageable pageable = PageRequest.of( page.intValue(), 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);

    }

    @Test
    public void should_return_deleted_admin_role_when_page_and_size_not_set(){
        Long page = null;
        Long size = null;
        String term = "A";
        Pageable pageable = PageRequest.of( 0, 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(rolePage.getTotalElements(), response.getTotalElements());
        verify(roleRepository, times(1)).findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);
    }

    @Test
    public void should_return_deleted_admin_role_when_term_and_page_are_not_set(){
        Long page = null;
        Long size = 10L;
        String term = "";
        Pageable pageable = PageRequest.of( 0, size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedTrueAndIsAdminTrue(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedTrueAndIsAdminTrue(pageable);
    }

    @Test
    public void should_return_deleted_admin_role_when_term_and_size_are_not_set(){
        Long page = 0L;
        Long size = null;
        String term = "";
        Pageable pageable = PageRequest.of( page.intValue(), 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedTrueAndIsAdminTrue(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedTrueAndIsAdminTrue(pageable);

    }

    @Test
    public void should_return_deleted_admin_role_when_term_are_not_set(){
        Long page = 0L;
        Long size = 10L;
        String term = "";
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedTrueAndIsAdminTrue(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedTrueAndIsAdminTrue(pageable);
    }

    @Test
    public void should_return_deleted_admin_role_when_params_set(){
        Long page = 0L;
        Long size = 10L;
        String term = "A";
        Pageable pageable = PageRequest.of( page.intValue(), size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);

    }



    // load or search deleted normal Roles

    @Test
    public void should_return_deleted_normal_role_when_page_is_not_set(){
        Long page = null;
        Long size = 10L;
        String term = "A";
        Pageable pageable = PageRequest.of( 0, size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);
    }

    @Test
    public void should_return_deleted_normal_role_when_size_is_not_set(){
        Long page = 0L;
        Long size = null;
        String term = "A";
        Pageable pageable = PageRequest.of( page.intValue(), 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);

    }

    @Test
    public void should_return_deleted_normal_role_when_page_and_size_not_set(){
        Long page = null;
        Long size = null;
        String term = "A";
        Pageable pageable = PageRequest.of( 0, 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(rolePage.getTotalElements(), response.getTotalElements());
        verify(roleRepository, times(1)).findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);
    }

    @Test
    public void should_return_deleted_normal_role_when_term_and_page_are_not_set(){
        Long page = null;
        Long size = 10L;
        String term = "";
        Pageable pageable = PageRequest.of( 0, size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedTrueAndIsAdminFalse(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedTrueAndIsAdminFalse(pageable);
    }

    @Test
    public void should_return_deleted_normal_role_when_term_and_size_are_not_set(){
        Long page = 0L;
        Long size = null;
        String term = "";
        Pageable pageable = PageRequest.of( page.intValue(), 10);

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedTrueAndIsAdminFalse(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedTrueAndIsAdminFalse(pageable);

    }

    @Test
    public void should_return_deleted_normal_role_when_term_are_not_set(){
        Long page = 0L;
        Long size = 10L;
        String term = "";
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findByIsDeletedTrueAndIsAdminFalse(pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(10, response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findByIsDeletedTrueAndIsAdminFalse(pageable);
    }

    @Test
    public void should_return_deleted_normal_role_when_params_set(){
        Long page = 0L;
        Long size = 10L;
        String term = "A";
        Pageable pageable = PageRequest.of( page.intValue(), size.intValue());

        Role role = Role.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .isAdmin(false)
                .build();
        RoleDto dto = new RoleDto(
                UUID.randomUUID(),
                "Admin",
                false,
                null
        );

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Page<Role> rolePage = new PageImpl<>(
                roleList,
                Pageable.ofSize(10).withPage(0),
                1
        );

//        Mock
        when(roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable))
                .thenReturn(rolePage);
        when(mapper.convertToRoleDto(any())).thenReturn(dto);

//        Action
        PageResponse<RoleDto> response = roleServices.loadOrSearchNotAdminNotActiveRolesPaged(page, size, term);
//        Then
        assertNotNull(response);
        assertEquals(rolePage.getContent().size(), response.getContent().size());
        assertEquals(page.intValue(), response.getCurrentPage());
        assertEquals(size.intValue(), response.getPageSize());
        assertEquals(rolePage.getTotalPages(), response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        verify(roleRepository, times(1)).findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);

    }
}