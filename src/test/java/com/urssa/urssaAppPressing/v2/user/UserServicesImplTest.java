package com.urssa.urssaAppPressing.v2.user;

import com.urssa.urssaAppPressing.v2.appConfig.execption.RessourcesNotFoundException;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.role.Role;
import com.urssa.urssaAppPressing.v2.role.RoleRepository;
import com.urssa.urssaAppPressing.v2.user.dto.AddUserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UpdateUserDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServicesImplTest {

    @InjectMocks
    private UserServicesImpl userServices;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    static void afterAll() {
        System.out.println("************ Users services tested *********");
    }


//    Load by id

    @Test
    public void should_return_user_by_id() {
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("6978874856")
                .email("admin@mail.com")
                .build();

        UserDto dto = new UserDto(
                userId,
                "Lorem",
                "xx",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "6978874856",
                null
        );

//        Mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.convertToUserDto(user)).thenReturn(dto);

//        Action
        UserDto response = userServices.loadUserById(userId);
//        Then
        assertNotNull(response);
        assertEquals(userId, response.getId());
    }

    @Test
    public void should_fail_when_user_id_is_null() {
        var exp = assertThrows(NullPointerException.class, () -> userServices.loadUserById(null));
        assertEquals("id_cannot_be_null_to_search", exp.getMessage());
    }

    @Test
    public void should_fail_when_user_not_found() {
        when(userRepository.findById(UUID.randomUUID())).thenReturn(null);
        var exp = assertThrows(RessourcesNotFoundException.class, ()-> userServices.loadUserById(UUID.randomUUID()));
        assertEquals("user_not_found", exp.getMessage());
    }


//    Add user
    @Test
    public void should_successfully_save_user() {
        UUID userId = UUID.randomUUID();
        String username = "admin";
        User user = User.builder()
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .password("xxxxxxxxx")
                .phone("6978874856")
                .email("admin@mail.com")
                .build();

        AddUserDto addUserDto = new AddUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "xxxxxxxxx",
                "6978874856",
                null
        );

        User saveUser = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .password("xxxxxxxxx")
                .phone("6978874856")
                .email("admin@mail.com")
                .build();

        UserDto dto = new UserDto(
                userId,
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "6978874856",
                null
        );

//        Mock calls
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(mapper.convertToUser(addUserDto)).thenReturn(user);
        when(passwordEncoder.encode(addUserDto.getPassword())).thenReturn(addUserDto.getPassword());
        when(userRepository.save(user)).thenReturn(saveUser);
        when(mapper.convertToUserDto(saveUser)).thenReturn(dto);
//        Action
        UserDto response = userServices.addUser(addUserDto);
//        Then
        assertEquals(addUserDto.getFirstName(), response.getFirstName());
        assertEquals(addUserDto.getUsername(), response.getUsername());
    }

    @Test
    public void should_fail_save_user_when_firstname_is_blank() {
        AddUserDto dto = new AddUserDto(
                "",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "XXXXXXXX123",
                "6978874856",
                null
        );

        var exp = assertThrows(IllegalArgumentException.class, ()-> userServices.addUser(dto));
        assertEquals("firstname_cannot_be_blank", exp.getMessage());
    }

    @Test
    public void should_fail_save_user_when_username_is_blank() {
        AddUserDto dto = new AddUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "",
                "XXXXXXXX123",
                "6978874856",
                null
        );

        var exp = assertThrows(IllegalArgumentException.class, ()-> userServices.addUser(dto));
        assertEquals("username_cannot_be_blank", exp.getMessage());
    }

    @Test
    public void should_fail_save_user_when_password_is_blank() {
        AddUserDto dto = new AddUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "",
                "6978874856",
                null
        );

        var exp = assertThrows(IllegalArgumentException.class, ()-> userServices.addUser(dto));
        assertEquals("password_cannot_be_blank", exp.getMessage());
    }

    @Test
    public void should_fail_save_user_when_username_already_exists() {

        UUID userId = UUID.randomUUID();
        String username = "admin";
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .build();

        UserDto dto = new UserDto(
                userId,
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "6978874856",
                null
        );

        AddUserDto addUserDto = new AddUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "XXXX4",
                "6978874856",
                null
        );

//        Mock calls
        when(userRepository.existsByUsername(username)).thenReturn(true);
//        Action
        var exp = assertThrows(IllegalArgumentException.class, ()-> userServices.addUser(addUserDto));
//        Then
        assertEquals("username_already_exists", exp.getMessage());
    }


//    Update user
    @Test
    public void should_fail_update_when_id_is_null() {
        UpdateUserDto updateUserDto = new UpdateUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "6978874856",
                null
        );

        var exp = assertThrows(NullPointerException.class, ()-> userServices.updateUser(updateUserDto, null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_update_when_user_not_found() {
        UUID userId = UUID.randomUUID();
        UpdateUserDto updateUserDto = new UpdateUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "6978874856",
                null
        );

        var exp = assertThrows(RessourcesNotFoundException.class, ()->userServices.updateUser(updateUserDto, userId));
        assertEquals("user_not_found", exp.getMessage());
    }

    @Test
    public void should_successfully_update_user() {
        UUID userId = UUID.randomUUID();
        UpdateUserDto updateUserDto = new UpdateUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "6978874856",
                null
        );

        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .build();

        UserDto dto = new UserDto(
                userId,
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "6978874856",
                null
        );
//        Mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.mapToUser(updateUserDto, user)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.convertToUserDto(user)).thenReturn(dto);
//       Action
        UserDto response = userServices.updateUser(updateUserDto, userId);
//      Then
        assertEquals(updateUserDto.getFirstName(), response.getFirstName());
        assertEquals(userId, response.getId());
        assertEquals(updateUserDto.getUsername(), response.getUsername());

    }


//    Delete
    @Test
    public void should_fail_soft_delete_when_id_is_null() {
        var exp = assertThrows(NullPointerException.class, ()-> userServices.softDeleteUser(null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_soft_delete_when_user_not_found() {
        when(userRepository.findById(UUID.randomUUID())).thenReturn(null);
        var exp = assertThrows(RessourcesNotFoundException.class, ()-> userServices.softDeleteUser(UUID.randomUUID()));
        assertEquals("user_not_found", exp.getMessage());
    }

    @Test
    public void should_successfully_soft_delete_user() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(null);
//        Action
        String response = userServices.softDeleteUser(userId);
        assertEquals("deleted_success", response);

    }

    @Test
    public void should_fail_delete_when_id_is_null() {

        var exp = assertThrows(NullPointerException.class, ()-> userServices.deleteUser(null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_delete_when_user_not_found() {
        when(userRepository.findById(UUID.randomUUID())).thenReturn(null);
        var exp = assertThrows(RessourcesNotFoundException.class, ()-> userServices.deleteUser(UUID.randomUUID()));
        assertEquals("user_not_found", exp.getMessage());
    }

    @Test
    public void should_successfully_delete_user() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        Action
        String response = userServices.deleteUser(userId);
        assertEquals("deleted_success", response);
    }


//    Update Password

    @Test
    public void should_fail_update_password_when_id_is_null() {
        var exp = assertThrows(NullPointerException.class, () -> userServices.updatePassword("XXXX", null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_update_password_when_password_is_blank() {
        var exp = assertThrows(IllegalArgumentException.class, ()->userServices.updatePassword("", UUID.randomUUID()));
        assertEquals("new_password_cannot_be_blank", exp.getMessage());
    }

    @Test
    public void should_fail_update_password_when_user_not_found() {
        when(userRepository.findById(UUID.randomUUID())).thenReturn(null);

        var exp = assertThrows(RessourcesNotFoundException.class, ()-> userServices.updatePassword("xxxx", UUID.randomUUID()));
        assertEquals("user_not_found", exp.getMessage());
    }

    @Test
    public void should_update_password() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("XXXX")).thenReturn("XXXX");
        when(userRepository.save(user)).thenReturn(user);
//        Action
        String response = userServices.updatePassword("XXXX", userId);
//        Then
        assertEquals("successfully_update", response);
    }

//    Update Roles
    @Test
    public void should_fail_update_role_when_id_is_null() {
        var exp = assertThrows(NullPointerException.class, () -> userServices.updateUserRoles(UUID.randomUUID(), null));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_update_role_when_role_id_not_found() {
        var exp = assertThrows(NullPointerException.class, () -> userServices.updateUserRoles(null, UUID.randomUUID()));
        assertEquals("role_id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_update_role_when_role_not_found() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(UUID.randomUUID())).thenReturn(null);
        var exp = assertThrows(RessourcesNotFoundException.class, ()-> userServices.updateUserRoles(UUID.randomUUID(), userId));
        assertEquals("role_not_found", exp.getMessage());
    }

    @Test
    public void should_fail_update_role_when_user_not_found() {

        when(userRepository.findById(UUID.randomUUID())).thenReturn(null);
        var exp = assertThrows(RessourcesNotFoundException.class, ()-> userServices.updateUserRoles(UUID.randomUUID(), UUID.randomUUID()));
        assertEquals("user_not_found", exp.getMessage());
    }

    @Test
    public void should_update_role() {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();
        Role role = Role.builder()
                .id(roleId)
                .name("Admin")
                .build();

        UserDto dto = new UserDto(
                userId,
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                role
        );

        User savedUser = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .role(role)
                .isDeleted(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(savedUser);
        when(mapper.convertToUserDto(savedUser)).thenReturn(dto);
//        Action
        UserDto response = userServices.updateUserRoles(roleId, userId);
//        Then
        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals(roleId, response.getRole().getId());
    }

//    Load active user

    @Test
    public void should_return_users_when_term_set() {
        String term= "x";
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findActiveUsersByFirstNameContainingIgnoreCase(term)).thenReturn(users);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        List<UserDto> response = userServices.loadOrSearchActiveUser(null,null, term);
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(userRepository,times(1)).findActiveUsersByFirstNameContainingIgnoreCase(term);
    }

    @Test
    public void should_return_users_when_term_is_not_set() {
        String term= "x";
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findByIsDeletedFalse()).thenReturn(users);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        List<UserDto> response = userServices.loadOrSearchActiveUser(null,null, "");
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(userRepository,times(1)).findByIsDeletedFalse();
    }


    @Test
    public void should_return_page_users_when_term_is_not_set() {
        String term= "x";
        Long page = 0L;
        Long size = 10L;
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());
        
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        Page<User> usersPage = new PageImpl<>(
                users,
                Pageable.ofSize(10).withPage(0),
                1
        );


        when(userRepository.findByIsDeletedFalse(pageable)).thenReturn(usersPage);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        PageResponse<UserDto> response = userServices.loadOrSearchActiveUserPaged(page, size,null,null, "");
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10, response.getPageSize());
        assertEquals(0, response.getCurrentPage());
    }


    @Test
    public void should_return_page_users_when_page_is_not_set() {
        String term= "x";
        Long page = 0L;
        Long size = 10L;
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        Page<User> usersPage = new PageImpl<>(
                users,
                Pageable.ofSize(10).withPage(0),
                1
        );


        when(userRepository.findActiveUsersByFirstNameContainingIgnoreCase(term, pageable)).thenReturn(usersPage);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        PageResponse<UserDto> response = userServices.loadOrSearchActiveUserPaged(null, size,null,null, term);
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10, response.getPageSize());
        assertEquals(0, response.getCurrentPage());
    }


    @Test
    public void should_return_page_users_when_size_is_not_set() {
        String term= "x";
        Long page = 0L;
        Long size = 10L;
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        Page<User> usersPage = new PageImpl<>(
                users,
                Pageable.ofSize(10).withPage(0),
                1
        );


        when(userRepository.findActiveUsersByFirstNameContainingIgnoreCase(term, pageable)).thenReturn(usersPage);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        PageResponse<UserDto> response = userServices.loadOrSearchActiveUserPaged(page, null,null,null, term);
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10, response.getPageSize());
        assertEquals(0, response.getCurrentPage());
    }

    @Test
    public void should_return_page_users_with_all_params() {
        String term= "x";
        Long page = 0L;
        Long size = 10L;
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        Page<User> usersPage = new PageImpl<>(
                users,
                Pageable.ofSize(10).withPage(0),
                1
        );


        when(userRepository.findActiveUsersByFirstNameContainingIgnoreCase(term, pageable)).thenReturn(usersPage);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        PageResponse<UserDto> response = userServices.loadOrSearchActiveUserPaged(page, size,null,null, term);
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10, response.getPageSize());
        assertEquals(0, response.getCurrentPage());
    }


//    Load deleted user
    @Test
    public void should_return_deleted_users_when_term_is_not_set() {
        Long page = 0L;
        Long size = 10L;
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        Page<User> usersPage = new PageImpl<>(
                users,
                Pageable.ofSize(10).withPage(0),
                1
        );


        when(userRepository.findByIsDeletedTrue(pageable)).thenReturn(usersPage);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        PageResponse<UserDto> response = userServices.loadOrSearchDeletedUserPaged(page, size,null,null, "");
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10, response.getPageSize());
        assertEquals(0, response.getCurrentPage());
    }


    @Test
    public void should_return_deleted_users_when_page_is_not_set() {
        String term= "x";
        Long page = 0L;
        Long size = 10L;
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        Page<User> usersPage = new PageImpl<>(
                users,
                Pageable.ofSize(10).withPage(0),
                1
        );


        when(userRepository.findDeletedUsersByFirstNameContainingIgnoreCase(term, pageable)).thenReturn(usersPage);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        PageResponse<UserDto> response = userServices.loadOrSearchDeletedUserPaged(null, size,null,null, term);
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10, response.getPageSize());
        assertEquals(0, response.getCurrentPage());
    }


    @Test
    public void should_return_deleted_users_when_size_is_not_set() {
        String term= "x";
        Long page = 0L;
        Long size = 10L;
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        Page<User> usersPage = new PageImpl<>(
                users,
                Pageable.ofSize(10).withPage(0),
                1
        );


        when(userRepository.findDeletedUsersByFirstNameContainingIgnoreCase(term, pageable)).thenReturn(usersPage);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        PageResponse<UserDto> response = userServices.loadOrSearchDeletedUserPaged(page, null,null,null, term);
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10, response.getPageSize());
        assertEquals(0, response.getCurrentPage());
    }

    @Test
    public void should_return_deleted_users_with_all_params() {
        String term= "x";
        Long page = 0L;
        Long size = 10L;
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .isDeleted(true)
                .build();

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "587456678",
                null
        );

        List<User> users = new ArrayList<>();
        users.add(user);

        Page<User> usersPage = new PageImpl<>(
                users,
                Pageable.ofSize(10).withPage(0),
                1
        );


        when(userRepository.findDeletedUsersByFirstNameContainingIgnoreCase(term, pageable)).thenReturn(usersPage);
        when(mapper.convertToUserDto(user)).thenReturn(dto);

        PageResponse<UserDto> response = userServices.loadOrSearchDeletedUserPaged(page, size,null,null, term);
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10, response.getPageSize());
        assertEquals(0, response.getCurrentPage());
    }


}