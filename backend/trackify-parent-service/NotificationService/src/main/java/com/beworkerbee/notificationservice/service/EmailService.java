package com.beworkerbee.notificationservice.service;

import com.beworkerbee.utils.dto.SendEmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(SendEmailDto sendEmailDto) {
        try {
            log.debug("Sending email message to "+sendEmailDto.getTo());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(sendEmailDto.getTo());
            message.setSubject(sendEmailDto.getSubject());
            message.setText(sendEmailDto.getContent());
            mailSender.send(message);
            log.info("Message sent successfully to: "+sendEmailDto.getTo());
        }
        catch (Exception e){
            log.error("An exception occurred while sending email to {}, exception: {} ",sendEmailDto.getTo(),e.getMessage());
        }
    }
}
