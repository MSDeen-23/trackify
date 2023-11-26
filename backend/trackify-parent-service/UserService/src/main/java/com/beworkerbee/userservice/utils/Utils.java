package com.beworkerbee.userservice.utils;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static String generateOtp(){
        return String.format("%06d", ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    public static long calculateTimeDifferenceInMinutes(Date minTime, Date maxTime) {
        return (maxTime.getTime() - minTime.getTime()) / (60 * 1000);
    }
}
