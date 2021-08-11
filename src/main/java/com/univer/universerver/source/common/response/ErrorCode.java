package com.univer.universerver.source.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND(404,"COMMON-ERR-404","페이지를 찾을 수 없습니다"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","서버 오류입니다"),
    EMAIL_DUPLICATION(400,"USER-ERR-400","이메일이 중복됩니다"),
	USERID_DUPLICATION(400,"USER-ERR-400","아이디가 중복됩니다"),
	NICKNAME_DUPLICATION(400,"USER-ERR-400","닉네임이 중복됩니다"),
	MATCHROOM_DUPLICATION(400,"USER-ERR-400","이미 방을 만들었습니다"),
	NOT_LOGIN(400,"USER-ERR-400","먼저 로그인을 해주세요");
	
    private int status;
    private String errorCode;
    private String message;
}
