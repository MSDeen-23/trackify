package com.beworkerbee.userservice.service;

import com.beworkerbee.userservice.dto.AuthenticateRequest;
import com.beworkerbee.userservice.dto.AuthenticationResponse;
import com.beworkerbee.userservice.dto.RegisterRequestAdmin;
import com.beworkerbee.userservice.dto.RegisterRequestUser;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequestAdmin request);

    AuthenticationResponse authenticate(AuthenticateRequest request);

    String registerUser(RegisterRequestUser request);
}
