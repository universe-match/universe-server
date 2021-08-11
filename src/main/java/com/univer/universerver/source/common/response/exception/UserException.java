package com.univer.universerver.source.common.response.exception;

import com.univer.universerver.source.common.response.ErrorCode;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException{

    private ErrorCode errorCode;

    public UserException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
