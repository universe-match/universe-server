package com.univer.universerver.source.common.response.exception;

import com.univer.universerver.source.common.response.ErrorCode;

import lombok.Getter;

@Getter
public class MatchRoomDuplicateException extends RuntimeException{
	private ErrorCode errorCode;

    public MatchRoomDuplicateException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
