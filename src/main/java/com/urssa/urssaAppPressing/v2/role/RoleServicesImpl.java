package com.urssa.urssaAppPressing.v2.role;

import com.urssa.urssaAppPressing.v2.appConfig.execption.RessourcesNotFoundException;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.permission.PermissionRepository;
import com.urssa.urssaAppPressing.v2.role.dto.AddRoleDto;
import com.urssa.urssaAppPressing.v2.role.dto.RoleDto;
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
    public List<RoleDto> loadOrSearchActiveRoles(String term) {
        List<Role> roles;

        if ((term != null) && !(term.isEmpty())) {
            roles = roleRepository.findActiveRolesByNameContainingIgnoreCaseAndNotAdmin(term);
        } else {
            roles = roleRepository.findAll();
        }

        return roles.stream()
                .map((mapper::convertToRoleDto))
                .toList();
    }

    @Override
    public PageResponse<RoleDto> loadOrSearchActiveRolesPaged(Long page, Long size, String term) {
        Page<Role> rolePage;

        if (page == null) { page = 0L; }
        if (size == null) { size = 10L; }

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());


        if (!(term ==null) && !(term.isEmpty())) {
            rolePage = roleRepository.findActiveRolesByNameContainingIgnoreCaseAndNotAdmin(term, pageable);
        } else {
            rolePage = roleRepository.findAll(pageable);
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
    public List<RoleDto> loadOrSearchRoles(String term) {
        List<Role> roles;

        if ((term != null) && !(term.isEmpty())) {
            roles = roleRepository.findByNameContainingIgnoreCaseAndAdmin(term);
        } else {
            roles = roleRepository.findAllActiveRoles();
        }

        return roles.stream()
                .map((mapper::convertToRoleDto))
                .toList();
    }

    @Override
    public PageResponse<RoleDto> loadOrSearchRolesPaged(Long page, Long size, String term) {
        Page<Role> rolePage;

        if (page == null) { page = 0L; }
        if (size == null) { size = 10L; }

        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());


        if (!(term ==null) && !(term.isEmpty())) {
            rolePage = roleRepository.findByNameContainingIgnoreCaseAndAdmin(term, pageable);
        } else {
            rolePage = roleRepository.findAllActiveRoles(pageable);
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
    public RoleDto loadRoleById(UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null_to_search");
        }

        return roleRepository.findById(id)
                .map(mapper::convertToRoleDto)
                .orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));
    }

    @Override
    public RoleDto addRoles(AddRoleDto request) {
        if ((request.getName() == null) || (request.getName().isEmpty())) {
            throw new IllegalArgumentException("name_cannot_be_null");
        }
        Role role = new Role();
        role.setName(request.getName());

        Set<UUID> permissionIds = Set.copyOf(request.getPermissions());
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(permissionIds)));

        Role savedRole = roleRepository.save(role);
        return mapper.convertToRoleDto(savedRole);
    }

    @Override
    public RoleDto updateRole(AddRoleDto request, UUID id) {
        if (id == null) {
            throw new NullPointerException("id_cannot_be_null");
        }
        if ((request.getName() == null) || (request.getName().isEmpty())) {
            throw new IllegalArgumentException("name_cannot_be_null");
        }

        Role role = roleRepository.findById(id).orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));

        Set<UUID> permissionIds = Set.copyOf(request.getPermissions());
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(permissionIds)));

        Role updatedRole = roleRepository.save(role);
        return mapper.convertToRoleDto(updatedRole);
    }

    @Override
    public String softDeleteRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new RessourcesNotFoundException("role_not_found"));
        role.setDeleted(true);
        return "delete_success";
    }

    @Override
    public String deleteRole(UUID id) {
        if (id == null){
            throw new NullPointerException("role_not_found");
        }
        roleRepository.deleteById(id);
        return "delete_success";
    }

    @Override
    public Role setAdminRole(UUID id) {
        if (id == null){
            throw new NullPointerException("role_not_found");
        }
        Role role = roleRepository.findById(id).orElseThrow(() ->new RessourcesNotFoundException("role_not_found"));
        role.setAdmin(true);

        return role;
    }

}
