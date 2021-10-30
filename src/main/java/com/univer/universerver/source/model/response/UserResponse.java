package com.univer.universerver.source.model.response;

import com.univer.universerver.source.model.Matching;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.UserImage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponse {

    private long id;

    private String nickname;

    private String age;

    private String mbti;

    private String universeName;

    private String major;

    private String profileImg;

    private String introduce;

    private List<UserImageResponse> userImages = new ArrayList<UserImageResponse>();

    public UserResponse(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.age = user.getAge();
        this.mbti = user.getMbti();
        this.universeName = user.getUniverseName();
        this.introduce = user.getIntroduce();
        this.major = user.getMajor();
        this.userImages = user.getUserImages().stream().map(item->new UserImageResponse(item)).collect(Collectors.toList());
//        this.profileImg = user.getProfileImg();
    }

    public UserResponse(Matching matchingUser){
        this.nickname = matchingUser.getUser().getNickname();
        this.age = matchingUser.getUser().getAge();
        this.mbti = matchingUser.getUser().getMbti();
        this.universeName = matchingUser.getUser().getUniverseName();
        this.userImages = matchingUser.getUser().getUserImages().stream().map(item->new UserImageResponse(item)).collect(Collectors.toList());

//        this.profileImg = matchingUser.getUser().getProfileImg();
    }

    public UserResponse(List<Matching> matchingList) {
        for (Matching matching : matchingList) {
            this.nickname = matching.getUser().getNickname();
            this.age = matching.getUser().getAge();
            this.mbti = matching.getUser().getMbti();
            this.universeName = matching.getUser().getUniverseName();
            this.userImages = matching.getUser().getUserImages().stream().map(item->new UserImageResponse(item)).collect(Collectors.toList());

//            this.profileImg = matching.getUser().getProfileImg();
        }
    }
}
