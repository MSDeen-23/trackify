package com.beworkerbee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterRequestAdmin extends RegisterRequestUser{
    @JsonProperty("organization_name")
    private String organizationName;
}
