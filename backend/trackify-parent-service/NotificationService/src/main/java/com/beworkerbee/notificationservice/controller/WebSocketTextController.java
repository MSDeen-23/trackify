package com.beworkerbee.notificationservice.controller;

import com.beworkerbee.utils.dto.SendPushNotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketTextController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody SendPushNotificationDTO sendPushNotificationDTO) {
        template.convertAndSend("/topic/"+sendPushNotificationDTO.userId(), sendPushNotificationDTO.message());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
