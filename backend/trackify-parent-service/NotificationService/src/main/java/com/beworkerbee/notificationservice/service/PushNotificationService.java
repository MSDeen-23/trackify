package com.beworkerbee.notificationservice.service;

import com.beworkerbee.utils.dto.SendPushNotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {
    @Autowired
    SimpMessagingTemplate template;

    public void sendPushNotification(SendPushNotificationDTO sendPushNotificationDTO){
        template.convertAndSend("/topic/"+sendPushNotificationDTO.userId(), sendPushNotificationDTO.message());
    }
}
