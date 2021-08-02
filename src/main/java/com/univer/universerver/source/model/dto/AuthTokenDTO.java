package com.univer.universerver.source.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthTokenDTO {
    String accessToken;
    String refreshToken;
}

