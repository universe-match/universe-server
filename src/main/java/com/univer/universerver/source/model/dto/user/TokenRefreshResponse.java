package com.univer.universerver.source.model.dto.user;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class TokenRefreshResponse {
	  private String accessToken;
	  private String refreshToken;
	  private String tokenType = "Bearer";

	  public TokenRefreshResponse(String accessToken, String refreshToken) {
	    this.accessToken = accessToken;
	    this.refreshToken = refreshToken;
	  }

	  // getters and setters
}