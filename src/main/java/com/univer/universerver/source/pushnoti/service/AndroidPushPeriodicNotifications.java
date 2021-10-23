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

        String sampleData[] = {"cpVfuEImS1WzhRqAOt0uf5:APA91bHy9p7KIx6QD78E2G4JF7kgBAo2Cmx0Yx-mQhXPm4PbbaJNal9YWOQLfdUpgnwpLTLLPYf7osKwNVVGNmtf471W1ibGTeR4kRD6au07fXBYT4bqAgVS9tJhZaCxMND-MoaN6VS2"};

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