package com.urssa.urssaAppPressing.v2.user;

import com.urssa.urssaAppPressing.v2.appConfig.execption.RessourcesNotFoundException;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.role.Role;
import com.urssa.urssaAppPressing.v2.role.RoleRepository;
import com.urssa.urssaAppPressing.v2.user.dto.AddUserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UpdateUserDto;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserServices {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Override
    public UserDto loadUserById(UUID id) {
        if (id == null){
            throw new NullPointerException("id_cannot_be_null_to_search");
        }

        return userRepository.findById(id)
                .map(mapper::convertToUserDto)
                .orElseThrow(()-> new RessourcesNotFoundException("user_not_found"));
    }

    @Override
    public UserDto addUser(AddUserDto request) {
        if (StringUtils.isBlank(request.getFirstName())) {
            throw new IllegalArgumentException("firstname_cannot_be_blank");
        }
        if (StringUtils.isBlank(request.getUsername())) {
            throw new IllegalArgumentException("username_cannot_be_blank");
        }
        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("password_cannot_be_blank");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("username_already_exists");
        }

        User user = mapper.convertToUser(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRole() != null) {
            Role role = roleRepository.findById(request.getRole())
                    .orElseThrow(() -> new RessourcesNotFoundException("role_to_attach_not_found"));

            user.setRole(role);
        }

        User savedUser = userRepository.save(user);
        return mapper.convertToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UpdateUserDto request, UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RessourcesNotFoundException("user_not_found"));
        user = mapper.mapToUser(request, user);

        User updateUser = userRepository.save(user);
        return mapper.convertToUserDto(updateUser);
    }

    @Override
    public String softDeleteUser(UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RessourcesNotFoundException("user_not_found"));

        user.setDeleted(true);

        try{
            userRepository.save(user);
            return "deleted_success";
        } catch (Exception e) {
            throw new InternalError("internal_server_error");
        }
    }

    @Override
    public String deleteUser(UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }
        userRepository.findById(id)
                .orElseThrow(() -> new RessourcesNotFoundException("user_not_found"));

        userRepository.deleteById(id);

        return "deleted_success";
    }

    @Override
    public String updatePassword(String newPassword, UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }
        if (Objects.equals(newPassword, "")) {
            throw new IllegalArgumentException("new_password_cannot_be_blank");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RessourcesNotFoundException("user_not_found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "successfully_update";
    }

    @Override
    public UserDto updateUserRoles(UUID roleId, UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }

        if (roleId == null) {
            throw new NullPointerException("role_id_cannot_be_null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RessourcesNotFoundException("user_not_found"));

        Role role =  roleRepository.findById(roleId)
                .orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));

        user.setRole(role);

        User savedUser = userRepository.save(user);

        return mapper.convertToUserDto(savedUser);
    }

    @Override
    public List<UserDto> loadOrSearchActiveUser(UUID companyId, UUID agencyId, String term) {
        List<User> users;

        if ((term != null) && !(term.isEmpty())) {
            users = userRepository.findActiveUsersByFirstNameContainingIgnoreCase(term);
        } else {
            users = userRepository.findByIsDeletedFalse();
        }

        return users.stream().map((mapper::convertToUserDto)).toList();
    }

    @Override
    public PageResponse<UserDto> loadOrSearchActiveUserPaged(Long page, Long size, UUID companyId, UUID agencyId, String term) {
        Page<User> userPages;
        
        if (page == null) { page = 0L;}
        if (size == null) { size = 10L;}

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());
        
        if ((term != null) && !(term.isEmpty())) {
            userPages = userRepository.findActiveUsersByFirstNameContainingIgnoreCase(term, pageable);
        } else {
            userPages = userRepository.findByIsDeletedFalse(pageable);
        }
        
        List<UserDto> userDtos = userPages.getContent().stream().map(mapper::convertToUserDto)
                .collect(Collectors.toList());
        
        return new PageResponse<>(
                userDtos,
                userPages.getTotalElements(),
                userPages.getTotalPages(),
                userPages.getNumber(),
                userPages.getSize()
        );
    }

    @Override
    public PageResponse<UserDto> loadOrSearchDeletedUserPaged(Long page, Long size, UUID companyId, UUID agencyId, String term) {
        Page<User> userPages;
        
        if (page == null) { page = 0L;}
        if (size == null) { size = 10L;}

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());
        
        if((term != null) && !(term.isEmpty())) {
            userPages = userRepository.findDeletedUsersByFirstNameContainingIgnoreCase(term, pageable);
        } else {
            userPages = userRepository.findByIsDeletedTrue(pageable);
        }

        List<UserDto> userDtos = userPages.getContent().stream().map(mapper::convertToUserDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                userDtos,
                userPages.getTotalElements(),
                userPages.getTotalPages(),
                userPages.getNumber(),
                userPages.getSize()
        );
    }
}
