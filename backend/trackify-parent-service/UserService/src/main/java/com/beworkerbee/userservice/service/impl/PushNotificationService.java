package com.beworkerbee.userservice.service.impl;

import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.userservice.repository.UserRepository;
import com.beworkerbee.userservice.service.NotificationService;
import com.beworkerbee.utils.dto.PushNotificationMessage;
import com.beworkerbee.utils.dto.SendPushNotificationDTO;
import com.beworkerbee.utils.kafkautils.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushNotificationService implements NotificationService {

    @Autowired
    private KafkaUtils kafkaUtils;

    private final String NOTIFICATION_TOPIC = "pushNotification";

    @Autowired
    private UserRepository userRepository;

    private void notify(SendPushNotificationDTO sendPushNotificationDTO){
        kafkaUtils.sendMessage(NOTIFICATION_TOPIC,sendPushNotificationDTO);
    }
    @Override
    public void notifyUser(User user, PushNotificationMessage pushNotificationMessage) {
        SendPushNotificationDTO sendPushNotificationDTO = new SendPushNotificationDTO
                (user.getId(),pushNotificationMessage);
        notify(sendPushNotificationDTO);

    }

    @Override
    public void notifyAdminOfUser(User user, PushNotificationMessage pushNotificationMessage) {
        SendPushNotificationDTO sendPushNotificationDTO = new SendPushNotificationDTO
                (user.getAdminUser().getId(),pushNotificationMessage);
        notify(sendPushNotificationDTO);
    }

    @Override
    public void notifyOrganizationOfUser(User user, PushNotificationMessage pushNotificationMessage) {
        List<User> userList = userRepository.findByOrganizationId(user.getOrganization().getId());
        userList.parallelStream().forEach(orgUser->{
            SendPushNotificationDTO sendPushNotificationDTO = new SendPushNotificationDTO(orgUser.getId(),pushNotificationMessage);
            notify(sendPushNotificationDTO);
        });
    }
}
