package com.univer.universerver.source.pushnoti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationService {

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    public void sendNoti(String nickName,String token,String title,String content){
        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(nickName,token,title,content);
        HttpHeaders resHeader = new HttpHeaders();
        resHeader.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<String> request = new HttpEntity<>(notifications,resHeader);

        //response.getWriter().print(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();
    }
}
