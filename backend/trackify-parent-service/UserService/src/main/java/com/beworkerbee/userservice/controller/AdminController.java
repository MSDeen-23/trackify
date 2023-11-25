package com.beworkerbee.userservice.controller;

import com.beworkerbee.userservice.dto.RegisterRequestUser;
import com.beworkerbee.userservice.service.impl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationServiceImpl authenticationService;
    @PostMapping("/create-new-user")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createNewUser(@Valid @RequestBody RegisterRequestUser request){
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

}
