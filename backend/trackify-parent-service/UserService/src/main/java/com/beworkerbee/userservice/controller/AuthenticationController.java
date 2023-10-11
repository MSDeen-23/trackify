package com.beworkerbee.userservice.controller;

import com.beworkerbee.userservice.dto.AuthenticateRequest;
import com.beworkerbee.userservice.dto.AuthenticationResponse;
import com.beworkerbee.userservice.dto.RegisterRequestUser;
import com.beworkerbee.userservice.dto.RegisterRequestAdmin;
import com.beworkerbee.userservice.entity.Organization;
import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.service.impl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    @PostMapping("/register-admin")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegisterRequestAdmin request
    ){
        return ResponseEntity.ok(
                authenticationService.register(request)
        );
    }

    @PostMapping("/create-new-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createNewUser(@Valid @RequestBody RegisterRequestUser request){
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestBody AuthenticateRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/init-validate-token")
    @Transactional(readOnly=true)
    public ResponseEntity<User> validateToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userDetails);
    }
}
