package com.beworkerbee.userservice.service;

import com.beworkerbee.userservice.entity.User;
import com.beworkerbee.utils.dto.PushNotificationMessage;

public interface NotificationService {
    void notifyUser(User user, PushNotificationMessage message);

    void notifyAdminOfUser(User user, PushNotificationMessage message);

    void notifyOrganizationOfUser(User user, PushNotificationMessage message);
}
