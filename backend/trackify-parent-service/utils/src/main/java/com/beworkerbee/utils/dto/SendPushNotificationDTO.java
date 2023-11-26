package com.beworkerbee.utils.dto;

import java.util.UUID;

public record SendPushNotificationDTO(UUID userId, PushNotificationMessage message) {
}
