package com.beworkerbee.leadsservice.annotations.impl;

import com.beworkerbee.leadsservice.annotations.Authorize;
import com.beworkerbee.leadsservice.domain.UserInfo;
import com.beworkerbee.leadsservice.exception.ForbiddenException;
import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Aspect
@Component
public class AuthorizeAspect {

    private final Gson gson = new Gson();
    @Around("@annotation(authorize)")
    public Object beforeMethodExecution(ProceedingJoinPoint joinPoint, Authorize authorize) throws Throwable {
        String role = authorize.role();
        if(StringUtils.hasText(role)) {
            String[] roles = role.split(",");
            UserInfo userInfo;
            try {
                userInfo = gson.fromJson(String.valueOf(joinPoint.getArgs()[0]), UserInfo.class);
            }catch (Exception e){
                throw  e;
            }
            boolean found = Arrays.stream(roles).anyMatch(r->r.equals(userInfo.getRoles()));
            if(!found){
                throw new ForbiddenException();
            }
        }
        return joinPoint.proceed();
    }
}