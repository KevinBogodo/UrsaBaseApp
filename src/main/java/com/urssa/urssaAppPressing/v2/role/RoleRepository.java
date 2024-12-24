package com.urssa.urssaAppPressing.v2.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    List<Role> findByNameContainingIgnoreCaseAndAdmin(@Param("term") String term);
    Page<Role> findByNameContainingIgnoreCaseAndAdmin(@Param("term") String term, Pageable pageable);

    List<Role> findActiveRolesByNameContainingIgnoreCaseAndNotAdmin(@Param("term") String term);
    Page<Role> findActiveRolesByNameContainingIgnoreCaseAndNotAdmin(@Param("term") String term, Pageable pageable);

    List<Role> findAllActiveRoles();
    Page<Role> findAllActiveRoles(Pageable pageable);

    Role findActiveRoleById(@Param("id") UUID id);
}
