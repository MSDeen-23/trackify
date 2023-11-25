package com.beworkerbee.emailservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SendEmailDto {
    private String to;
    private String subject;
    private String content;
}
