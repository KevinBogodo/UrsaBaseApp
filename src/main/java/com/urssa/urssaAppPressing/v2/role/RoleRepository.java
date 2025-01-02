package com.urssa.urssaAppPressing.v2.role;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    List<Role> findByIsDeletedFalseAndIsAdminFalse();
    Page<Role> findByIsDeletedFalseAndIsAdminFalse(Pageable pageable);
    List<Role> findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(String term);
    Page<Role> findActiveRolesByNameContainingIgnoreCaseAndIsAdminFalse(String term, Pageable pageable);

    List<Role> findByIsDeletedFalseAndIsAdminTrue();
    Page<Role> findByIsDeletedFalseAndIsAdminTrue(Pageable pageable);
    List<Role> findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(String term);
    Page<Role> findActiveRolesByNameContainingIgnoreCaseAndIsAdminTrue(String term, Pageable pageable);

    Page<Role> findByIsDeletedTrueAndIsAdminTrue(Pageable pageable);
    Page<Role> findDeletedRolesByNameContainingIgnoreCaseAndIsAdminTrue(String term, Pageable pageable);

    Page<Role> findByIsDeletedTrueAndIsAdminFalse(Pageable pageable);

    Page<Role> findDeletedRolesByNameContainingIgnoreCaseAndIsAdminFalse(String term, Pageable pageable);

    boolean existsByName(@NotBlank(message = "name_cannot_be_null") String name);
}
