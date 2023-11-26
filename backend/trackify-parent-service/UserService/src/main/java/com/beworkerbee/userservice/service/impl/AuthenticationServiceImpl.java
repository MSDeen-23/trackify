package com.beworkerbee.userservice.service.impl;

import com.beworkerbee.userservice.config.JwtService;
import com.beworkerbee.userservice.dto.*;
import com.beworkerbee.userservice.entity.InactiveReason;
import com.beworkerbee.userservice.entity.Organization;
import com.beworkerbee.userservice.entity.Role;
import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.exception.AlreadyExistsException;
import com.beworkerbee.userservice.exception.OtpVerificationException;
import com.beworkerbee.userservice.repository.OrganizationRepository;
import com.beworkerbee.userservice.repository.UserRepository;
import com.beworkerbee.userservice.service.AuthenticationService;
import com.beworkerbee.userservice.service.ISpecification;
import com.beworkerbee.userservice.service.NotificationService;
import com.beworkerbee.userservice.service.impl.validations.OrganizationExistsSpecification;
import com.beworkerbee.userservice.service.impl.validations.UserExistsSpecification;
import com.beworkerbee.userservice.utils.Utils;
import com.beworkerbee.utils.dto.NotificationType;
import com.beworkerbee.utils.dto.PushNotificationMessage;
import com.beworkerbee.utils.dto.SendEmailDto;
import com.beworkerbee.utils.kafkautils.KafkaUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticate;

    @Autowired
    private KafkaUtils kafkaUtils;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public User registerAdmin(RegisterRequestAdmin request) {

        log.debug("Creating new user with email address :{}", request.getEmail());
        log.debug("Organization name : {} ", request.getOrganizationName());

        String verifyOtp = Utils.generateOtp();
        // Creating new user
        User user = User.builder()
                .active(false)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .verifyOtp(verifyOtp)
                .verifyOtpCreatedTime(new Date())
                .inactiveReason(InactiveReason.NOT_VERIFIED)
                .build();

        // Creating new organization
        Organization organization = Organization.builder()
                .organizationName(request.getOrganizationName())
                .adminUser(user)
                .active(true)
                .build();

        // validate the organization and user
        List<ISpecification> validations = Arrays.asList(
                new UserExistsSpecification(userRepository, user), // checks if user with the email already present
                new OrganizationExistsSpecification(organizationRepository, organization) // checks if the given organization is already present
        );
        for (ISpecification specification : validations) {
            if(specification.isSatisfied()){
                log.error("Specification satisfied "+specification.getClass().getName());
                throw new AlreadyExistsException(specification.getEntity());
            }
        }

        // set the organization of the user and save
        user.setOrganization(organization);

        // save organization
        organizationRepository.save(organization);
        // save user
        String jwtToken = jwtService.generateToken(user);
        user.setJwtToken(jwtToken);
        userRepository.save(user);
        return user;
    }

    public User authenticate(AuthenticateRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        authenticate.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Map<String, Object> claims = new HashMap();
        claims.put("roles", user.getRole());
        String jwtToken = jwtService.generateToken(claims, user);
        user.setJwtToken(jwtToken);
        if(user.getAdminUser()!=null) {
            PushNotificationMessage pushNotificationMessage =
                    new PushNotificationMessage(NotificationType.INFO, "User: " + user.getFirstName() + " logged in!");
            notificationService.notifyAdminOfUser(user, pushNotificationMessage);
        }
        return user;
    }

    @Override
    public String registerUser(RegisterRequestUser request) {
        // check if user already exists
        log.debug("Registering a new user");
        if (userRepository.existsByEmailAndActiveIsTrue(request.getEmail())) {
            log.error("The email {} is already used", request.getEmail());
            throw new AlreadyExistsException("User");
        }

        // get the admin credentials from spring security (JWT)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User adminUser = (User) authentication.getPrincipal();

        // from token get the admin and the organization details
        User user = userRepository.findById(adminUser.getId()).get();
        Organization organization = organizationRepository.findById(adminUser.getOrganization().getId()).get();

        // generate the verify otp
        String verifyOtp = Utils.generateOtp();

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .organization(organization)
                .adminUser(user)
                .role(Role.USER)
                .active(false)
                .verifyOtp(verifyOtp)
                .verifyOtpCreatedTime(new Date())
                .inactiveReason(InactiveReason.NOT_VERIFIED)
                .build();

        // save the new user
        userRepository.save(newUser);

        // Send a verification email
        log.info("Sending verification email to "+request.getEmail());
        SendEmailDto sendEmailDto = SendEmailDto.builder()
                .to(request.getEmail())
                .subject("Email otp")
                .content("Welcome to trackify.io your otp is "+verifyOtp)
                .build();
        kafkaUtils.sendMessage("emailNotification",sendEmailDto);

        // notify entire organization as new user has been added
        PushNotificationMessage pushNotificationMessage =
                new PushNotificationMessage(NotificationType.INFO,"A new user added in your organization");
        notificationService.notifyOrganizationOfUser(user,pushNotificationMessage);

        return "User created successfully";
    }

    @Override
    @Transactional
    public String resendOtp(User user) {
        log.debug("Resending email otp to user: "+user.getEmail());
        String verifyOtp = Utils.generateOtp();
        user.setVerifyOtp(verifyOtp);
        user.setVerifyOtpCreatedTime(new Date());
        userRepository.save(user);
        SendEmailDto sendEmailDto = SendEmailDto.builder()
                .to(user.getEmail())
                .subject("Email otp")
                .content("Your new otp is "+verifyOtp)
                .build();
        kafkaUtils.sendMessage("emailNotification",sendEmailDto);
        log.info("Email otp sent to user: "+user.getEmail());
        return "OTP for is sent to the mail address: "+user.getEmail();
    }

    @Override
    @Transactional
    public User verifyOtp(User user, VerifyOtpDto verifyOtpDto) {
        if (isOtpValid(user, verifyOtpDto.otp())) {
            long timeDifferenceInMin = Utils.calculateTimeDifferenceInMinutes(user.getVerifyOtpCreatedTime(), new Date());
            if (timeDifferenceInMin < 30) {
                user.setActive(true);
                user.setInactiveReason(null);
                User savedUser = userRepository.save(user);

                PushNotificationMessage pushNotificationMessage =
                        new PushNotificationMessage(NotificationType.INFO,"User: "+user.getEmail()+" is verified!");
                notificationService.notifyAdminOfUser(user,pushNotificationMessage);

                return savedUser;
            } else {
                resendOtp(user);
                throw new OtpVerificationException("Oops! It seems the verification time has lapsed. No worries, we've resent the OTP. Please check your mailbox.");
            }
        } else {
            throw new OtpVerificationException("Oops! The entered OTP is incorrect. Please verify and retry.");
        }
    }

    @Override
    public String resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = userRepository.findByEmail(resetPasswordDto.email())
                .orElseThrow(() -> new UsernameNotFoundException(resetPasswordDto.email()+" not found"));
        String otp = Utils.generateOtp();
        user.setVerifyOtp(otp);
        user.setVerifyOtpCreatedTime(new Date());
        userRepository.save(user);
        SendEmailDto sendEmailDto = SendEmailDto.builder()
                .to(resetPasswordDto.email())
                .subject("Reset password")
                .content("Your otp for password reset is : "+otp)
                .build();
        kafkaUtils.sendMessage("emailNotification",sendEmailDto);
        return "OTP sent successfully!";
    }

    @Override
    public String setNewPassword(SetNewPasswordDto setNewPasswordDto) {
        User user = userRepository.findByEmail(setNewPasswordDto.email())
                .orElseThrow(() -> new UsernameNotFoundException(setNewPasswordDto.email()+" not found"));
        if (isOtpValid(user, setNewPasswordDto.otp())) {
            long timeDifferenceInMin = Utils.calculateTimeDifferenceInMinutes(user.getVerifyOtpCreatedTime(), new Date());
            if (timeDifferenceInMin < 30) {
                user.setPassword(passwordEncoder.encode(setNewPasswordDto.password()));
                userRepository.save(user);
                return "The password has been reset successfully.";
            } else {
                resendOtp(user);
                throw new OtpVerificationException("Oops! It seems the verification time has lapsed. No worries, we've resent the OTP. Please check your mailbox.");
            }
        }
        else{
            throw new OtpVerificationException("Oops! The entered OTP is incorrect. Please verify and retry.");
        }

    }

    private boolean isOtpValid(User user, String otp) {
        return user.getVerifyOtp().equals(otp);
    }

}
