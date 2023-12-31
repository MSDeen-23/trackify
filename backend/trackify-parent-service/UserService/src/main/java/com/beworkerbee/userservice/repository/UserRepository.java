package com.beworkerbee.userservice.repository;

import com.beworkerbee.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmailAndActiveIsTrue(String email);

    List<User> findByOrganizationId(UUID orgId);
}
