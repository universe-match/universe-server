package com.univer.universerver.source.pushnoti.service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AndroidPushPeriodicNotifications {
    public static String PeriodicNotificationJson(String nickName,String token,String title,String content) throws JSONException {
        LocalDate localDate = LocalDate.now();

        String sampleData[] = {token};

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
        notification.put("title",title);
        notification.put("body",content);

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}