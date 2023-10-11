package com.beworkerbee.userservice.service;

import com.beworkerbee.userservice.dto.AuthenticateRequest;
import com.beworkerbee.userservice.dto.AuthenticationResponse;
import com.beworkerbee.userservice.dto.RegisterRequestAdmin;
import com.beworkerbee.userservice.dto.RegisterRequestUser;
import com.beworkerbee.userservice.entity.User;

public interface AuthenticationService {
    User register(RegisterRequestAdmin request);

    User authenticate(AuthenticateRequest request);

    String registerUser(RegisterRequestUser request);
}
