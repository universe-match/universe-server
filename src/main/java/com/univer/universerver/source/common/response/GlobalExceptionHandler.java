package com.univer.universerver.source.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.univer.universerver.source.common.response.exception.EmailDuplicateException;
import com.univer.universerver.source.common.response.exception.UserDuplicateException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicateException(EmailDuplicateException ex){
        log.error("handleEmailDuplicateException",ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleUserIdDuplicateException(UserDuplicateException ex){
        log.error("handleUserDuplicateException",ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("handleException",ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}