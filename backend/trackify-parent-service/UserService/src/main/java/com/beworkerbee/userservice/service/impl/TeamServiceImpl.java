package com.beworkerbee.userservice.service.impl;

import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.repository.UserRepository;
import com.beworkerbee.userservice.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> getTeamOfUser(User user) {
        log.debug("Getting team members of user "+user.getId());
        List<User> allUsers = userRepository.findByOrganizationId(user.getOrganization().getId());
        return allUsers;
    }
}
