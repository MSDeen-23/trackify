package com.beworkerbee.userservice.service.impl.validations;

import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.exception.AlreadyExistsException;
import com.beworkerbee.userservice.repository.UserRepository;
import com.beworkerbee.userservice.service.ISpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
public class UserExistsSpecification implements ISpecification {

    private final UserRepository userRepository;

    private final User user;

    public UserExistsSpecification(UserRepository userRepository, User user) {
        this.userRepository = userRepository;
        this.user = user;
    }

    @Override
    public String getEntity() {
        return "User";
    }

    @Override
    public boolean isSatisfied() {
        if(userRepository.existsByEmailAndActiveIsTrue(user.getEmail())){
            return true;
        }
        return false;
    }

}
