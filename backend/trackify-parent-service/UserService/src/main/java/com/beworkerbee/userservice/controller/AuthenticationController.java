package com.beworkerbee.userservice.controller;

import com.beworkerbee.userservice.dto.AuthenticateRequest;
import com.beworkerbee.userservice.dto.AuthenticationResponse;
import com.beworkerbee.userservice.dto.RegisterRequestUser;
import com.beworkerbee.userservice.dto.RegisterRequestAdmin;
import com.beworkerbee.userservice.service.impl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    @PostMapping("/register-admin")
    public ResponseEntity<AuthenticationResponse> register(
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
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticateRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
