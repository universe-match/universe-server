package com.univer.universerver.source.model.dto;

import com.univer.universerver.source.model.User;

public class UserDTO {

	private String nickname;
	
	public UserDTO(User user) {
		this.nickname = user.getNickname();
	}
}
