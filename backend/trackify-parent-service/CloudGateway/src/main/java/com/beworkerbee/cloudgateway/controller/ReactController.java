package com.beworkerbee.cloudgateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReactController {

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String get(){
        return "forward:/";
    }
}
