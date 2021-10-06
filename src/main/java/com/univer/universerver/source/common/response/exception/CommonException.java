package com.univer.universerver.source.common.response.exception;

import com.univer.universerver.source.common.response.ErrorCode;

public class CommonException extends RuntimeException{

    private ErrorCode errorCode;

    public CommonException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

}
