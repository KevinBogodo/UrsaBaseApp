package com.urssa.urssaAppPressing.v2.role;

import com.urssa.urssaAppPressing.v2.appConfig.execption.RessourcesNotFoundException;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.permission.Permission;
import com.urssa.urssaAppPressing.v2.permission.PermissionRepository;
import com.urssa.urssaAppPressing.v2.role.dto.AddRoleDto;
import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServicesImpl implements RoleServices{

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper mapper;

    @Override
    public RoleDto loadRoleById(UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null_to_search");
        }

        return roleRepository.findById(id)
                .map(mapper::convertToRoleDto)
                .orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));
    }

    @Override
    public RoleDto addRoles(AddRoleDto request, String type) {
        if (StringUtils.isBlank(request.getName())) {
            throw new IllegalArgumentException("name_cannot_be_blank");
        }
        if (roleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("name_already_exists");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setAdmin(Objects.equals(type, "admin"));

        if (request.getPermissions() != null) {
            Set<UUID> permissionIds = Set.copyOf(request.getPermissions());
            List<Permission> permissions = permissionRepository.findAllById(permissionIds);

            if (permissions.size() != permissionIds.size()) {
                throw new IllegalArgumentException("Some permissions are invalid");
            }

            role.setPermissions(new HashSet<>(permissions));
        }


        Role savedRole = roleRepository.save(role);
        return mapper.convertToRoleDto(savedRole);
    }


    @Override
    public RoleDto updateRole(AddRoleDto request, UUID id) {
        if (request == null) {
            throw new IllegalArgumentException("request_data_cannot_be_empty");
        }
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }
        if ((request.getName() == null) || (request.getName().isEmpty())) {
            throw new IllegalArgumentException("name_cannot_be_empty");
        }

        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));

        Set<UUID> permissionIds = Set.copyOf(request.getPermissions());
        role.setName(request.getName());
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(permissionIds)));

        Role updatedRole = roleRepository.save(role);
        return mapper.convertToRoleDto(updatedRole);
    }

    @Override
    public String softDeleteRole(UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }

        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));
        role.setDeleted(true);

        try {
            roleRepository.save(role);
            return "delete_success";
        } catch (Exception e) {
            throw new InternalError("internal_server_error");
        }

    }

    @Override
    public String deleteRole(UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }
        roleRepository.findById(id)
                .orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));

        roleRepository.deleteById(id);
        return "delete_success";
    }

    @Override
    public RoleDto restoreRole(UUID id){
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }

        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));
        role.setDeleted(false);

        Role restoredRole = roleRepository.save(role);
        return mapper.convertToRoleDto(restoredRole);
    };

    @Override
    public RoleDto changeRoleType(UUID id, String type){
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }
        if (type.isEmpty()) {
            throw new IllegalArgumentException("type_cannot_be_undefined");
        }

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RessourcesNotFoundException("role_not_found"));

        role.setAdmin(Objects.equals(type, "admin"));

        Role updatedRole = roleRepository.save(role);

        return mapper.convertToRoleDto(updatedRole);
    };

    @Override
    public List<RoleDto> loadOrSearchNotAdminActiveRoles(String term) {
        List<Role> roles;

        if ((term != null) && !(term.isEmpty())) {
            roles = roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term);
        } else {
            roles = roleRepository.findByIsDeletedFalseAndIsAdminFalse();
        }

        return roles.stream()
                .map((mapper::convertToRoleDto))
                .toList();
    }

    @Override
    public  PageResponse<RoleDto> loadOrSearchNotAdminActiveRolesPaged(Long page, Long size, String term){
        Page<Role> rolePage;

        if (page == null) { page = 0L; }
        if (size == null) { size = 10L; }

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());


        if (!(term ==null) && !(term.isEmpty())) {
            rolePage = roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);
        } else {
            rolePage = roleRepository.findByIsDeletedFalseAndIsAdminFalse(pageable);
        }

        List<RoleDto> rolesDto = rolePage.getContent()
                .stream().map(mapper::convertToRoleDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                rolesDto,
                rolePage.getTotalElements(),
                rolePage.getTotalPages(),
                rolePage.getNumber(),
                rolePage.getSize()
        );
    }

    @Override
    public List<RoleDto> loadOrSearchAdminActiveRoles(String term) {
        List<Role> roles;

        if ((term != null) && !(term.isEmpty())) {
            roles = roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term);
        } else {
            roles = roleRepository.findByIsDeletedFalseAndIsAdminTrue();
        }

        return roles.stream()
                .map((mapper::convertToRoleDto))
                .toList();
    }

    @Override
    public PageResponse<RoleDto> loadOrSearchAdminActiveRolesPaged(Long page, Long size, String term) {
        Page<Role> rolePage;

        if (page == null) { page = 0L; }
        if (size == null) { size = 10L; }

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());


        if (!(term ==null) && !(term.isEmpty())) {
            rolePage = roleRepository.findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);
        } else {
            rolePage = roleRepository.findByIsDeletedFalseAndIsAdminTrue(pageable);
        }

        List<RoleDto> rolesDto = rolePage.getContent()
                .stream().map(mapper::convertToRoleDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                rolesDto,
                rolePage.getTotalElements(),
                rolePage.getTotalPages(),
                rolePage.getNumber(),
                rolePage.getSize()
        );
    }

    @Override
    public PageResponse<RoleDto> loadOrSearchAdminNotActiveRolesPaged(Long page, Long size, String term) {
        Page<Role> rolePage;

        if (page == null) { page = 0L; }
        if (size == null) { size = 10L; }

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());


        if (!(term ==null) && !(term.isEmpty())) {
            rolePage = roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(term, pageable);
        } else {
            rolePage = roleRepository.findByIsDeletedTrueAndIsAdminTrue(pageable);
        }

        List<RoleDto> rolesDto = rolePage.getContent()
                .stream().map(mapper::convertToRoleDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                rolesDto,
                rolePage.getTotalElements(),
                rolePage.getTotalPages(),
                rolePage.getNumber(),
                rolePage.getSize()
        );
    }

    @Override
    public PageResponse<RoleDto> loadOrSearchNotAdminNotActiveRolesPaged(Long page, Long size, String term) {
        Page<Role> rolePage;

        if (page == null) { page = 0L; }
        if (size == null) { size = 10L; }

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());


        if (!(term ==null) && !(term.isEmpty())) {
            rolePage = roleRepository.findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(term, pageable);
        } else {
            rolePage = roleRepository.findByIsDeletedTrueAndIsAdminFalse(pageable);
        }

        List<RoleDto> rolesDto = rolePage.getContent()
                .stream().map(mapper::convertToRoleDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                rolesDto,
                rolePage.getTotalElements(),
                rolePage.getTotalPages(),
                rolePage.getNumber(),
                rolePage.getSize()
        );
    }




}
