package com.urssa.urssaAppPressing.v2.user;

import com.urssa.urssaAppPressing.v2.user.dto.AddUserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UpdateUserDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp(){
        mapper = new UserMapper();
    }

    @AfterAll
    static void afterAll() {
        System.out.println("*********************UserMapper tested ok ************************");
    }


//    Convert to user Dto

    @Test
    public void should_fail_convert_to_userDto_when_user_is_null() {
        var exp = assertThrows(NullPointerException.class, ()->mapper.convertToUserDto(null));
        assertEquals("user_is_null", exp.getMessage());
    }

    @Test
    public void should_fail_convert_to_userDto_when_id_is_null() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(null)
                .firstName("Lorem")
                .lastName("xx")
                .username("admin")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()-> mapper.convertToUserDto(user));
        assertEquals("id_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_fail_convert_to_userDto_when_username_is_blank() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()-> mapper.convertToUserDto(user));
        assertEquals("username_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_fail_convert_to_userDto_when_firstname_is_blank() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("")
                .lastName("xx")
                .username("Admin")
                .password("")
                .phone("587456678")
                .email("admin@mail.com")
                .build();

        var exp = assertThrows(IllegalArgumentException.class, ()-> mapper.convertToUserDto(user));
        assertEquals("firstName_cannot_be_empty", exp.getMessage());
    }


    @Test
    public void should_convert_to_userDto() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .password("")
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

        UserDto response = mapper.convertToUserDto(user);

        assertNotNull(response, "response_cannot_be_null");
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals(user.getUsername(), response.getUsername());
    }


//   Convert to user from addUser dto

    @Test
    public void should_fail_convert_to_user_when_user_is_null() {
        var exp = assertThrows(NullPointerException.class, ()->mapper.convertToUser(null));
        assertEquals("request_to_convert_is_null", exp.getMessage());
    }

    @Test
    public void should_fail_convert_to_user_when_username_is_blank() {
        AddUserDto dto = new AddUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "",
                "XXXXXXXXX123",
                "6978874856",
                null
        );

        var exp = assertThrows(IllegalArgumentException.class, ()-> mapper.convertToUser(dto));
        assertEquals("username_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_fail_convert_to_user_when_firstname_is_blank() {
        AddUserDto dto = new AddUserDto(
                "",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "Admin",
                "XXXXXXXXX123",
                "6978874856",
                null
        );

        var exp = assertThrows(IllegalArgumentException.class, ()-> mapper.convertToUser(dto));
        assertEquals("name_cannot_be_null", exp.getMessage());
    }


    @Test
    public void should_convert_to_user() {
        AddUserDto dto = new AddUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "XXXXXXXX123",
                "6978874856",
                null
        );

        User response = mapper.convertToUser(dto);

        assertNotNull(response, "response_cannot_be_null");
        assertEquals(dto.getFirstName(), response.getFirstName());
        assertEquals(dto.getLastName(), response.getLastName());
        assertEquals(dto.getUsername(), response.getUsername());
    }


//    Map to user from dto

    @Test
    public void should_fail_mapper_to_user_when_user_is_null() {
        var exp = assertThrows(NullPointerException.class, ()->mapper.mapToUser(null,null));
        assertEquals("request_to_convert_is_null", exp.getMessage());
    }

    @Test
    public void should_fail_mapper_to_user_when_username_is_blank() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("")
                .phone("587456678")
                .email("admin@mail.com")
                .build();

        UpdateUserDto dto = new UpdateUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "",
                "6978874856",
                null
        );

        var exp = assertThrows(IllegalArgumentException.class, ()-> mapper.mapToUser(dto, user));
        assertEquals("username_cannot_be_empty", exp.getMessage());
    }

    @Test
    public void should_fail_mapper_to_user_when_firstname_is_blank() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("")
                .password("xxxxxxxxx")
                .phone("587456678")
                .email("admin@mail.com")
                .build();

        UpdateUserDto dto = new UpdateUserDto(
                "",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "Admin",
                "6978874856",
                null
        );

        var exp = assertThrows(IllegalArgumentException.class, ()-> mapper.mapToUser(dto, user));
        assertEquals("name_cannot_be_null", exp.getMessage());
    }

    @Test
    public void should_mapper_to_user() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Lorem")
                .lastName("xx")
                .username("Admin")
                .phone("587456678")
                .email("admin@mail.com")
                .build();

        UpdateUserDto dto = new UpdateUserDto(
                "Lorem",
                "XX",
                new Date(),
                "M",
                "admin@mail.com",
                "admin",
                "6978874856",
                null
        );

        User response = mapper.mapToUser(dto, user);

        assertNotNull(response, "response_cannot_be_null");
        assertEquals(dto.getFirstName(), response.getFirstName());
        assertEquals(dto.getLastName(), response.getLastName());
        assertEquals(dto.getUsername(), response.getUsername());
    }
}