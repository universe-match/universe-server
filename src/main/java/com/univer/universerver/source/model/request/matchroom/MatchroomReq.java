package com.univer.universerver.source.model.request.matchroom;

import lombok.Getter;

@Getter
public class MatchroomReq {
	
	private String title;
    
	private String content;
    
	private char groupKind;
    
	private String place;
    
	private long peopleLimit;
    
	private String genderKind;
	
	private String fromDate;
	
	private String toDate;
	
	private long[] friends;
}
