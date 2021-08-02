package com.univer.universerver.source.service;

import com.univer.universerver.source.exception.GeneralErrorException;
import com.univer.universerver.source.exception.TokenAuthenticationException;
import com.univer.universerver.source.model.AuthToken;
import com.univer.universerver.source.model.dto.AuthTokenDTO;
import com.univer.universerver.source.repository.AuthTokenRepository;
import com.univer.universerver.source.security.jwt.AuthTokenType;
import com.univer.universerver.source.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthTokenService {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthTokenRepository authTokenRepository;

    public AuthTokenDTO createAuthToken(String userid) {
        AuthToken authToken = new AuthToken();
        authToken.setUserid(userid);
        //authTokenRepository.save(authToken);

        String accessToken = jwtProvider.generateJwtToken(authToken.getId(), userid, AuthTokenType.accessToken);
        String refreshToken = jwtProvider.generateJwtToken(authToken.getId(), userid, AuthTokenType.refreshToken);

        AuthTokenDTO authTokenDTO = new AuthTokenDTO();
        authTokenDTO.setAccessToken(accessToken);
        authTokenDTO.setRefreshToken(refreshToken);
        return authTokenDTO;
    }


    public AuthTokenDTO refresh(String refreshToken) {
        try {
            jwtProvider.assertAuthToken(refreshToken, AuthTokenType.refreshToken);
        } catch (TokenAuthenticationException e) {
            throw new GeneralErrorException();
        }

        Long id = jwtProvider.getTokenId(refreshToken);
        AuthTokenType authTokenType = jwtProvider.getTokenType(refreshToken);

        if (AuthTokenType.refreshToken == authTokenType) {
            throw new GeneralErrorException();
        }

        // 토큰 DB 존재 확인, user_id와 매칭 되는지도 확인 필요
        AuthToken token = authTokenRepository.findById(id).orElseThrow(() -> new GeneralErrorException());


        // 토큰 갱신
        String updateAccessToken = jwtProvider.generateJwtToken(id, token.getUserid(), AuthTokenType.accessToken);
        String updateRefreshToken = jwtProvider.generateJwtToken(id, token.getUserid(), AuthTokenType.refreshToken);

        AuthTokenDTO authTokenDTO = new AuthTokenDTO();
        authTokenDTO.setAccessToken(updateAccessToken);
        authTokenDTO.setRefreshToken(updateRefreshToken);
        return authTokenDTO;
    }

    public void logout(String tokenValue) {
        Long id = jwtProvider.getTokenId(tokenValue);
        authTokenRepository.deleteById(id);
    }
}

