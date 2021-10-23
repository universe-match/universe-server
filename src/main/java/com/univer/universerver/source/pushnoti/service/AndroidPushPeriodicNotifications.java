package com.univer.universerver.source.pushnoti.service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AndroidPushPeriodicNotifications {
    public static String PeriodicNotificationJson() throws JSONException {
        LocalDate localDate = LocalDate.now();

        String sampleData[] = {"eO5PrXXYvkRQsEpt4dQzzf:APA91bEH_9K7U7PGVnwyN-dvrWujm8pheN1_lEfhShQu4jF4l4jVid_FxexrZX-DOJQ2pY8C9820VcDFhyoT4xyYBXRtp1Jpq7eJuhuUBfC59wm7mGnDhR4e2dqMXbK0Y1Li6f_KTwDy"};

        JSONObject body = new JSONObject();

        List<String> tokenlist = new ArrayList<String>();

        for(int i=0; i<sampleData.length; i++){
            tokenlist.add(sampleData[i]);
        }

        JSONArray array = new JSONArray();

        for(int i=0; i<tokenlist.size(); i++) {
            array.put(tokenlist.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title","hello world!");
        notification.put("body","Today is Good "+localDate.getDayOfWeek().name()+"!");

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}