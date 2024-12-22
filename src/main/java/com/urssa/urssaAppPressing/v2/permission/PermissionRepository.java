package com.urssa.urssaAppPressing.v2.permission;

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
}
