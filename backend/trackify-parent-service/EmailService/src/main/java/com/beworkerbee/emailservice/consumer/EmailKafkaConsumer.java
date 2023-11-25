package com.beworkerbee.emailservice.consumer;


import com.beworkerbee.emailservice.dto.SendEmailDto;
import com.beworkerbee.emailservice.service.EmailService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailKafkaConsumer {

    @Autowired
    private EmailService emailService;
    @KafkaListener(topics = "emailNotification",groupId = "email_group")
    public void consume(String message){

            log.info("Kafka consumed : " + message);

            // deserialize to SendEmailDto
            log.debug("Deserializing to SendEmailDto");
            SendEmailDto sendEmailDto = new Gson().fromJson(message, SendEmailDto.class);
            log.info("Deserialized the message to SendEmailDto");

            // send email
            emailService.sendEmail(sendEmailDto);

    }
}
