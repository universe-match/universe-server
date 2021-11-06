package com.univer.universerver.source.model.response;

import com.univer.universerver.source.model.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AdminUserResponse {

    private long id;

    private String nickname;

    private String age;

    private String mbti;

    private String universeName;

    private String major;

    private String introduce;

    private boolean verified;

    private String deleteYn;

    private List<UserImageResponse> userImages = new ArrayList<UserImageResponse>();

    private String universeCertiImg;
    private String apply;

    private String rejectContent;

    public AdminUserResponse(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.age = user.getAge();
        this.mbti = user.getMbti();
        this.universeName = user.getUniverseName();
        this.major = user.getMajor();
        this.introduce = user.getIntroduce();
        this.apply = user.getApply();
        this.rejectContent = user.getRejectContent();
        this.deleteYn = user.getDeleteYn();
        this.userImages = user.getUserImages().stream().map(item->new UserImageResponse(item)).collect(Collectors.toList());
        this.universeCertiImg = user.getUniverseCertiImg();
    }
}
