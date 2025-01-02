package com.urssa.urssaAppPressing.v2.permission;

import com.urssa.urssaAppPressing.v2.appConfig.execption.RessourcesNotFoundException;
import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.permission.dto.AddPermissionDto;
import com.urssa.urssaAppPressing.v2.permission.dto.PermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PermissionServicesImpl implements PermissionServices{

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionsMapper;

    @Override
    public List<PermissionDto> loadOrSearchAllPermission(String term) {
        List<Permission> data;

        if (term != null && !term.isEmpty()) {
            data =  permissionRepository.findByNameContainingIgnoreCase(term);
        } else {
            data = permissionRepository.findAll();
        }

        return data.stream()
                .map(permissionsMapper::convertToDto)
                .toList();
    }

    @Override
    public PageResponse<PermissionDto> loadOrSearchAllPermissionPaginated(Long page, Long size, String term) {
        if (page == null) { page = 0L;}
        if (size == null) { size = 10L;}
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());
        Page<Permission> permissionPage;

        if (term != null && !term.isEmpty()) {
            permissionPage = permissionRepository.findByNameContainingIgnoreCase(term, pageable);
        } else {
            permissionPage = permissionRepository.findAll(pageable);
        }

        List<PermissionDto> permissionDto = permissionPage.getContent().stream()
                .map(permissionsMapper::convertToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                permissionDto,
                permissionPage.getTotalElements(),
                permissionPage.getTotalPages(),
                permissionPage.getNumber(),
                permissionPage.getSize()
        );
    }

    @Override
    public PermissionDto loadPermissionById(UUID id) {
        if (id == null){
            throw new NullPointerException("id_cannot_be_null");
        }
        return permissionRepository.findById(id).map(permissionsMapper::convertToDto)
                .orElseThrow(() -> new RessourcesNotFoundException("permission_not_found"));
    }

    @Override
    public PermissionDto addPermission(AddPermissionDto addPermissionDTO) {
        if (addPermissionDTO.getCode() == null || addPermissionDTO.getCode().isEmpty()){
            throw new IllegalArgumentException("code_cannot_be_empty");
        }
        if (addPermissionDTO.getName() == null || addPermissionDTO.getName().isEmpty()){
            throw new IllegalArgumentException("name_cannot_be_empty");
        }
        if (permissionRepository.existsByName(addPermissionDTO.getName())) {
            throw new IllegalArgumentException("name_already_exists");
        }
        if (permissionRepository.existsByCode(addPermissionDTO.getCode())) {
            throw new IllegalArgumentException("code_already_exists");
        }
        Permission permission = permissionsMapper.convertToPermission(addPermissionDTO);
        Permission savedPermissions = permissionRepository.save(permission);
        return permissionsMapper.convertToDto(savedPermissions);
    }

    @Override
    public PermissionDto updatePermission(AddPermissionDto updatedData, UUID id) {
        if (id == null){
            throw new NullPointerException("id_cannot_be_null");
        }
        if (updatedData == null){
            throw new NullPointerException("update_data_object_cannot_be_empty");
        }
        if (updatedData.getCode() == null || updatedData.getCode().isEmpty()){
            throw new IllegalArgumentException("code_cannot_be_empty");
        }
        if (updatedData.getName() == null || updatedData.getName().isEmpty()){
            throw new IllegalArgumentException("name_cannot_be_empty");
        }

        Permission permission = permissionRepository
                .findById(id)
                .orElseThrow(() ->new RessourcesNotFoundException("permission_not_found"));

        permission.setName(updatedData.getName());
        permission.setCode(updatedData.getCode());
        Permission savedPermission = permissionRepository.save(permission);

        return permissionsMapper.convertToDto(savedPermission);
    }

}
