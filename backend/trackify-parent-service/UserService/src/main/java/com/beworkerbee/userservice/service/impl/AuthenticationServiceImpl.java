package com.beworkerbee.userservice.service.impl;

import com.beworkerbee.userservice.config.JwtService;
import com.beworkerbee.userservice.dto.AuthenticateRequest;
import com.beworkerbee.userservice.dto.AuthenticationResponse;
import com.beworkerbee.userservice.dto.RegisterRequestUser;
import com.beworkerbee.userservice.dto.RegisterRequestAdmin;
import com.beworkerbee.userservice.entity.Organization;
import com.beworkerbee.userservice.entity.Role;
import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.exception.AlreadyExistsException;
import com.beworkerbee.userservice.repository.OrganizationRepository;
import com.beworkerbee.userservice.repository.UserRepository;
import com.beworkerbee.userservice.service.AuthenticationService;
import com.beworkerbee.userservice.service.ISpecification;
import com.beworkerbee.userservice.service.impl.validations.OrganizationExistsSpecification;
import com.beworkerbee.userservice.service.impl.validations.UserExistsSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticate;

    @Override
    @Transactional
    public User register(RegisterRequestAdmin request) {

        log.debug("Creating new user with email address :{}", request.getEmail());
        log.debug("Organization name : {} ", request.getOrganizationName());

        // Creating new user
        User user = User.builder()
                .active(true)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        // Creating new organization
        Organization organization = Organization.builder()
                .organizationName(request.getOrganizationName())
                .adminUser(user)
                .active(true)
                .build();

        // validate the organization and user
        List<ISpecification> validations = Arrays.asList(
                new UserExistsSpecification(userRepository, user), // checks if user with the email already present
                new OrganizationExistsSpecification(organizationRepository, organization) // checks if the given organization is already present
        );
        for (ISpecification specification : validations) {
            if(specification.isSatisfied()){
                log.error("Specification satisfied "+specification.getClass().getName());
                throw new AlreadyExistsException(specification.getEntity());
            }
        }

        // set the organization of the user and save
        user.setOrganization(organization);

        // save organization
        organizationRepository.save(organization);
        // save user
        String jwtToken = jwtService.generateToken(user);
        user.setJwtToken(jwtToken);
        userRepository.save(user);



        return user;


    }

    public User authenticate(AuthenticateRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        authenticate.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Map<String, Object> claims = new HashMap();
        claims.put("roles", user.getRole());
        String jwtToken = jwtService.generateToken(claims, user);
        user.setJwtToken(jwtToken);
        return user;
    }

    @Override
    public String registerUser(RegisterRequestUser request) {
        // check if user already exists
        log.debug("Registering a new user");
        if (userRepository.existsByEmailAndActiveIsTrue(request.getEmail())) {
            log.error("The email {} is already used", request.getEmail());
            throw new AlreadyExistsException("User");
        }

        // get the admin credentials from spring security (JWT)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User adminUser = (User) authentication.getPrincipal();
        User user = userRepository.findById(adminUser.getId()).get();
        Organization organization = organizationRepository.findById(adminUser.getOrganization().getId()).get();

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .organization(organization)
                .adminUser(user)
                .role(Role.USER)
                .active(true)
                .build();

        userRepository.save(newUser);

        return "User created successfully";
    }
}
