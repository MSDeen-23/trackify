package com.beworkerbee.userservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<String> demo(){
        return ResponseEntity.ok("Hello");
    }
}
