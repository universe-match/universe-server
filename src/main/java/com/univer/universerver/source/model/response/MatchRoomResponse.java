package com.univer.universerver.source.model.response;

import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.Matching;
import com.univer.universerver.source.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MatchRoomResponse {

    private long id;

    private String title;

    private String content;

    private String groupKind;

    private String place;

    private long peopleLimit;

    private String genderKind;

    private String fromDate;

    private String toDate;

    private String nickName;

    private List<UserResponse> matchingList;

    private long matchRoomLen;

    public MatchRoomResponse(MatchRoom matchRoom) {
        this.id = matchRoom.getId();
        this.title = matchRoom.getTitle();
        this.content = matchRoom.getContent();
        this.groupKind = matchRoom.getGroupkind();
        this.place = matchRoom.getPlace();
        this.peopleLimit = matchRoom.getPeopleLimit();
        this.genderKind = matchRoom.getGenderkind();
        this.fromDate = matchRoom.getFromDate();
        this.toDate = matchRoom.getToDate();
        this.nickName = matchRoom.getUser().getNickname();
        this.matchingList = matchRoom.getMatchingList().stream().map(item->new UserResponse(item)).collect(Collectors.toList());
        this.matchRoomLen = matchRoom.getMatchingList().size();
        //this.matchingList = new UserResponse(matchRoom.getMatchingList());
    }
}
