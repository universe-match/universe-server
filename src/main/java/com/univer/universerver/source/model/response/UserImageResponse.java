package com.univer.universerver.source.model.response;

import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.UserImage;
import lombok.Getter;

@Getter
public class UserImageResponse {

    private String userImage;

    public UserImageResponse(UserImage userImage){
        this.userImage = userImage.getProfileImg();
    }

}
