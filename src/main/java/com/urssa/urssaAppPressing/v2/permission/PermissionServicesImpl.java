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
    private final PermisionsMapper permisionsMapper;

    @Override
    public List<PermissionDto> loadOrSearchAllPermission(String term) {
        List<Permission> data;

        if (term != null && !term.isEmpty()) {
            data =  permissionRepository.findByNameContainingIgnoreCase(term);
        } else {
            data = permissionRepository.findAll();
        }

        return data.stream()
                .map(permission -> new PermissionDto(
                        permission.getId(),
                        permission.getCode(),
                        permission.getCode(),
                        permission.getCreatedBy(),
                        permission.getCreatedAt(),
                        permission.getUpdatedAt()))
                .toList();
    }

    @Override
    public PageResponse<PermissionDto> loadOrSearchAllPermissionPaginated(Long page, Long size, String term) {
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue());
        Page<Permission> permissionPage;

        if (term != null && !term.isEmpty()) {
            permissionPage = permissionRepository.findByNameContainingIgnoreCase(term, pageable);
        } else {
            permissionPage = permissionRepository.findAll(pageable);
        }

        List<PermissionDto> permissionDto = permissionPage.getContent().stream()
                .map(permisionsMapper::convertToDto)
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
        return permissionRepository.findById(id).map(permisionsMapper::convertToDto)
                .orElseThrow(() -> new RessourcesNotFoundException("permission_not_found"));
    }

    @Override
    public PermissionDto addPermission(AddPermissionDto addPermissionDTO) {

        Permission permission = permisionsMapper.convertToPermission(addPermissionDTO);
        Permission savedPermissions = permissionRepository.save(permission);
        return permisionsMapper.convertToDto(savedPermissions);
    }

    @Override
    public PermissionDto updatePermission(AddPermissionDto updatedData, UUID id) {
        Permission savedPermission;
        Permission permissionData = permisionsMapper.convertToPermission(updatedData);

        Optional<Permission> existingPermission = permissionRepository.findById(id);

        if (existingPermission.isPresent()) {
            Permission permissionToUpdate = existingPermission.get();
            permissionToUpdate.setName(permissionData.getName());
            permissionToUpdate.setCode(permissionData.getCode());
            savedPermission = permissionRepository.save(permissionToUpdate);
        } else {
            savedPermission = permissionRepository.save(permissionData);
        }

        return permisionsMapper.convertToDto(savedPermission);
    }
}
