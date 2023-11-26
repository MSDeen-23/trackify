package com.beworkerbee.notificationservice.consumer;

import com.beworkerbee.notificationservice.service.PushNotificationService;
import com.beworkerbee.utils.dto.SendPushNotificationDTO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PushNotificationKafkaConsumer {

    @Autowired
    private PushNotificationService pushNotificationService;
    @KafkaListener(topics = "pushNotification",groupId = "push_group")
    public void consume(String message){

        log.info("Kafka consumed : " + message);

        // deserialize to SendEmailDto
        log.debug("Deserializing to SendPushNotificationDTO");
        SendPushNotificationDTO sendPushNotificationDTO = new Gson().fromJson(message, SendPushNotificationDTO.class);
        log.info("Deserialized the message to SendPushNotificationDTO");

        // send email
        pushNotificationService.sendPushNotification(sendPushNotificationDTO);

    }
}
