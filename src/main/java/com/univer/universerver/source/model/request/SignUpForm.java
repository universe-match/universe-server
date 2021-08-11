package com.univer.universerver.source.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SignUpForm {

    @NotBlank
    @Size(min = 3, max = 50)
    private String userid;
    @NotBlank
    @Size(min = 3, max = 50)
    private String nickname;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private String gender;

    private String age;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

  //  private String profileimagePaths;

}
