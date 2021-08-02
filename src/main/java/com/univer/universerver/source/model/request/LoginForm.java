package com.univer.universerver.source.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginForm {

    @NotBlank
    @Size(min=3, max = 60)
    private String userid;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

   // private String fcmToken;
}
