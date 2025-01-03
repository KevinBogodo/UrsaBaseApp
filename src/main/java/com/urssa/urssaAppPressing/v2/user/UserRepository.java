package com.urssa.urssaAppPressing.v2.user;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    List<User> findByIsDeletedFalse();
    Page<User> findByIsDeletedFalse(Pageable pageable);
    List<User> findActiveUsersByFirstNameContainingIgnoreCase(String term);
    Page<User> findActiveUsersByFirstNameContainingIgnoreCase(String term, Pageable pageable);

    Page<User> findByIsDeletedTrue(Pageable pageable);
    Page<User> findDeletedUsersByFirstNameContainingIgnoreCase(String term, Pageable pageable);


    boolean existsByUsername(@NotBlank(message = "username_cannot_be_null") String username);
}
