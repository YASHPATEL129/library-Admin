package com.libraryAdmin.helper;


import com.libraryAdmin.consts.AppConfigs;
import com.libraryAdmin.hashRepository.UserSessionTrackerRepo;
import com.libraryAdmin.model.hashes.UserSessionTracker;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.util.UUID;

public class SystemHelper {

    public static String generateVerificationCode(){
        return RandomStringUtils.randomNumeric(6).toUpperCase();
    }

    public static String generateUsername() {
        StringBuffer data =
                new StringBuffer().append(UUID.randomUUID().toString().replace("-", ""))
                        .append(UUID.randomUUID().toString().replace("-", ""))
                        .append(UUID.randomUUID().toString().replace("-", ""))
                        .append(UUID.randomUUID().toString().replace("-", ""));
        SecureRandom random = new SecureRandom();
        int index = random.nextInt(100);
        int len = index + 12;
        return data.substring(index, len).toLowerCase();
    }

    public static Boolean validateDevice(HttpServletRequest request, String userEmail, UserSessionTrackerRepo userSessionTrackerRepo){
        String deviceType = request.getHeader(AppConfigs.DEVICE_TYPE_NAME);
        String deviceToken = request.getHeader(AppConfigs.DEVICE_VERIFICATION_TOKEN);
        if (!ObjectUtils.isEmpty(deviceType) && !StringUtils.isEmpty(deviceType)){
            UserSessionTracker session = userSessionTrackerRepo.getSession(userEmail , deviceType);
            return (session != null && StringUtils.equals(session.getVerificationId(), deviceToken));
        }
        return false;
    }

    public static String generateFileName(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}