package com.urssa.urssaAppPressing.v2.permission;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    List<Permission> findByNameContainingIgnoreCase(String name);
    Page<Permission> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByCode(@NotBlank(message = "code_cannot_be_empty") String code);

    boolean existsByName(@NotBlank(message = "name_cannot_be_empty") String name);
}
