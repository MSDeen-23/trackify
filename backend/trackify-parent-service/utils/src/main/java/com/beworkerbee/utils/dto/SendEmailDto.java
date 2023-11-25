package com.beworkerbee.utils.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailDto {
    private String to;
    private String subject;
    private String content;
}
