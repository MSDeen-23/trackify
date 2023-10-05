package com.beworkerbee.userservice.service.impl;

import com.beworkerbee.userservice.config.JwtService;
import com.beworkerbee.userservice.dto.AuthenticateRequest;
import com.beworkerbee.userservice.dto.AuthenticationResponse;
import com.beworkerbee.userservice.dto.RegisterRequestUser;
import com.beworkerbee.userservice.dto.RegisterRequestAdmin;
import com.beworkerbee.userservice.entity.Organization;
import com.beworkerbee.userservice.entity.Role;
import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.exception.OrganizationAlreadyPresentException;
import com.beworkerbee.userservice.exception.UserAlreadyExistsException;
import com.beworkerbee.userservice.repository.OrganizationRepository;
import com.beworkerbee.userservice.repository.UserRepository;
import com.beworkerbee.userservice.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    public AuthenticationResponse register(RegisterRequestAdmin request) {

            log.debug("Creating new user with email address :{}", request.getEmail());
            log.debug("Organization name : {} ", request.getOrganizationName());

            // check if user is already present in the system
            if(userRepository.existsByEmail(request.getEmail())){
                log.error("User already present in the system");
                throw new UserAlreadyExistsException();
            }
            // Creating new user
            User user = User.builder()
                    .active(true)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();


            // Creating organization
            if(organizationRepository.existsByOrganizationName(request.getOrganizationName())){
                throw new OrganizationAlreadyPresentException();
            }
            Organization organization = Organization.builder()
                    .organizationName(request.getOrganizationName())
                    .adminUser(user)
                    .active(true)
                    .build();


            // set the organization of the user and save
            user.setOrganization(organization);

            // save organization
            organizationRepository.save(organization);
            // save user
            userRepository.save(user);

            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();


    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        authenticate.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        Map<String, Object> claims = new HashMap();
        claims.put("role",user.getRole());
        String jwtToken = jwtService.generateToken(claims,user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public String registerUser(RegisterRequestUser request) {
        User user  = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return "User created successfully";
    }
}
