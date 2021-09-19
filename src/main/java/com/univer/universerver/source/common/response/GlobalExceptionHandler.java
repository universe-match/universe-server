package com.univer.universerver.source.common.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.univer.universerver.source.common.response.exception.MatchRoomDuplicateException;
import com.univer.universerver.source.common.response.exception.UserException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(EmailDuplicateException.class)
//    public ResponseEntity<ErrorResponse> handleEmailDuplicateException(EmailDuplicateException ex){
//        log.error("handleEmailDuplicateException",ex);
//        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
//        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
//    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserIdDuplicateException(UserException ex){
        log.error("handleUserDuplicateException",ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(MatchRoomDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleMatchRoomDuplicateException(MatchRoomDuplicateException ex){
        log.error("handleMacthRoomDuplicateException",ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> BadCredential(Exception ex){
//        log.error("handleException",ex);
//        ErrorResponse response = new ErrorResponse(ErrorCode.ERR_ID_PASSWORD);
//        return new ResponseEntity<>(response, HttpStatus.);
//    }
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    	ErrorResponse response = new ErrorResponse(ErrorCode.ACCESS_DENIED);
    	return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
    }
    //bad credential 예외처리
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
    	ErrorResponse response = new ErrorResponse(ErrorCode.ERR_ID_PASSWORD);
    	return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("handleException",ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}