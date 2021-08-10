package com.univer.universerver.source.common.response.exception;

import com.univer.universerver.source.common.response.ErrorCode;

import lombok.Getter;

@Getter
public class EmailDuplicateException extends RuntimeException{

    private ErrorCode errorCode;

    public EmailDuplicateException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}