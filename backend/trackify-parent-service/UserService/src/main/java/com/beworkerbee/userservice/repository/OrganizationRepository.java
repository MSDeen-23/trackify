package com.beworkerbee.userservice.repository;

import com.beworkerbee.userservice.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    boolean existsByOrganizationName(String organizationName);
}
