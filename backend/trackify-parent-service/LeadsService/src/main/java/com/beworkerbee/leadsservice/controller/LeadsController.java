package com.beworkerbee.leadsservice.controller;

import com.beworkerbee.leadsservice.annotations.Authorize;
import com.beworkerbee.leadsservice.domain.UserInfo;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
public class LeadsController {

    private final Gson gson = new Gson();
    @GetMapping
    @Authorize(role = "USER")
    public String leadsTest(@RequestHeader(value="userDetails") String userAgent){
        UserInfo userInfo = gson.fromJson(userAgent, UserInfo.class);
        return userInfo.toString();
    }
}
