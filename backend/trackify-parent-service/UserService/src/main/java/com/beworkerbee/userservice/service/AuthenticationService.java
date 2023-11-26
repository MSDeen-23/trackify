package com.beworkerbee.userservice.service;

import com.beworkerbee.userservice.dto.*;
import com.beworkerbee.userservice.entity.User;

public interface AuthenticationService {
    User registerAdmin(RegisterRequestAdmin request);

    User authenticate(AuthenticateRequest request);

    String registerUser(RegisterRequestUser request);

    String resendOtp(User user);

    User verifyOtp(User user, VerifyOtpDto otp);

    String resetPassword(ResetPasswordDto resetPasswordDto);

    String setNewPassword(SetNewPasswordDto setNewPasswordDto);
}
