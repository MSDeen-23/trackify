package com.beworkerbee.userservice.controller;

import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/organization")
@RequiredArgsConstructor
public class DemoController {
    private final DemoService demoService;
//    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ADMIN')")
//    public ResponseEntity<String> hello(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return demoService.demo();
//    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> hello(@RequestParam UUID orgId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        userDetails.getOrganization();
        return demoService.demo();
    }
}
