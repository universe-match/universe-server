package com.univer.universerver.source.model.dto;

import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class MatchroomDTO {
	
	private String title;
    
	private String content;
    
	private char groupKind;
    
	private String place;
    
	private long peopleLimit;
    
	private String genderKind;
	
	private String fromDate;
	
	private String toDate;
	
	private String nickName;
	
	
	public MatchroomDTO(MatchRoom matchRoom) {
		this.title = matchRoom.getTitle();
		this.content = matchRoom.getContent();
		this.groupKind = matchRoom.getGroupkind();
		this.place = matchRoom.getPlace();
		this.peopleLimit = matchRoom.getPeopleLimit();
		this.genderKind = matchRoom.getGenderkind();
		this.fromDate = matchRoom.getFromDate();
		this.toDate = matchRoom.getToDate();
		this.nickName = matchRoom.getUser().getNickname();
	}
}
