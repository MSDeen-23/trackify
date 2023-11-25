package com.beworkerbee.userservice.service.impl.validations;

import com.beworkerbee.userservice.service.ISpecification;
import com.beworkerbee.userservice.entity.Organization;
import com.beworkerbee.userservice.repository.OrganizationRepository;


public class OrganizationExistsSpecification implements ISpecification {

    private final OrganizationRepository organizationRepository;

    private final Organization organization;

    public OrganizationExistsSpecification(OrganizationRepository organizationRepository, Organization organization) {
        this.organizationRepository = organizationRepository;
        this.organization = organization;
    }

    @Override
    public String getEntity() {
        return "Organization";
    }

    @Override
    public boolean isSatisfied() {
        if(organizationRepository.existsByOrganizationName(organization.getOrganizationName())){
            return true;
        }
        return false;

    }
}
