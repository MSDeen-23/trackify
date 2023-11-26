package com.beworkerbee.userservice.service;

import com.beworkerbee.userservice.entity.User;

import java.util.List;

public interface TeamService {
    List<User> getTeamOfUser(User user);
}
