package com.beworkerbee.leadsservice.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class UserInfo {
    private String roles;
    private String userId;
    private String orgId;
}
