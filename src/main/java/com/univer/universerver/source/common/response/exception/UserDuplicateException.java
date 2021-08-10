package com.univer.universerver.source.common.response.exception;

import com.univer.universerver.source.common.response.ErrorCode;

import lombok.Getter;

@Getter
public class UserDuplicateException extends RuntimeException{

    private ErrorCode errorCode;

    public UserDuplicateException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
