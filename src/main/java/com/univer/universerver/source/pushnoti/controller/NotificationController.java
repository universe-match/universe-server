package com.univer.universerver.source.pushnoti.controller;

import com.univer.universerver.source.pushnoti.service.AndroidPushNotificationsService;
import com.univer.universerver.source.pushnoti.service.AndroidPushPeriodicNotifications;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class NotificationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @GetMapping(value = "/send")
    public @ResponseBody ResponseEntity<String> send(HttpServletResponse response) throws JSONException, InterruptedException  {
        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson();
        HttpHeaders resHeader = new HttpHeaders();
        resHeader.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<String> request = new HttpEntity<>(notifications,resHeader);

        //response.getWriter().print(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try{
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        }
        catch (InterruptedException e){
            logger.debug("got interrupted!");
            throw new InterruptedException();
        }
        catch (ExecutionException e){
            logger.debug("execution error!");
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
