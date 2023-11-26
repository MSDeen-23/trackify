package com.beworkerbee.userservice.controller;

import com.beworkerbee.userservice.dto.*;
import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.exception.UserNotVerifiedException;
import com.beworkerbee.userservice.service.impl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
                authenticationService.registerAdmin(request)
        );
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
        if(!userDetails.getActive()){
            throw new UserNotVerifiedException(userDetails.getEmail());
        }
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        return ResponseEntity.ok(authenticationService.resendOtp(userDetails));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<User> resendOtp(@RequestBody VerifyOtpDto verifyOtp){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        return ResponseEntity.ok(authenticationService.verifyOtp(userDetails,verifyOtp));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto){

        return ResponseEntity.ok(authenticationService.resetPassword(resetPasswordDto));
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<String> resetPassword(@RequestBody SetNewPasswordDto setNewPasswordDto){

        return ResponseEntity.ok(authenticationService.setNewPassword(setNewPasswordDto));
    }


}
