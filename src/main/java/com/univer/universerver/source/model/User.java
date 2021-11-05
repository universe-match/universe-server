package com.univer.universerver.source.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.univer.universerver.source.model.request.SignUpForm;
import com.univer.universerver.source.utils.DateAudit;


@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "nickname"
        }),
        @UniqueConstraint(columnNames = {
                "userid"
        })
})

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends DateAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max = 50)
    @Column(name="nickname")
    private String nickname;

    @NaturalId
    @NotBlank
    @Size(min=2, max = 20)
    @Column(name="userid")
    private String userid;

    @Size(max = 50)
    @Email
    @Column(name="email")
    private String email;
    @JsonIgnore
    @Column(name="gender")
    private String gender;
    @JsonIgnore
    @Column(name="age")
    private String age;
    
    @Column(name="mbti")
    private String mbti;

    @Column(name="blood")
    private String blood;
    
    @Column(name="stature")
    private String stature;
    
    @Column(name="valid_user")
    private String validUser;
    
    @Column(name="universeName")
    private String universeName;//대학이름
    
    @Column(name="major")
    private String major;//전

    @Column(name="universe_certiimg")
    private String universeCertiImg;//학생

    @NotBlank
    @Size(min=6, max = 100)
    @JsonIgnore
    @Column(name="password")
    private String password;

    @Lob
    @Column(name="introduce")
    private String introduce;


    @Column(name="report_cnt")
    @ColumnDefault("0")
    private long reporteCnt;

    @Column(name="account_suspend")
    @ColumnDefault("0")
    private long accountSuspend;

    @Column(name="fcmtoken")
    private String fcmToken;

    @Column(name="last_access_time")
    private LocalDateTime lastaccesstime;

    @Column(name="alarm_check")
    @ColumnDefault("0")
    @Convert(converter=BooleanToYNConverter.class)
    private boolean alarmCheck;

    @Convert(converter=BooleanToYNConverter.class)
    private boolean verified = false;
    
    @Column(name="point")
    @ColumnDefault("0")
    private long point;
    @Column(name="delete_yn" ,columnDefinition = "varchar(1) default 'N'")
    private String deleteYn="N";
    @Column(name="noti_yn" ,columnDefinition = "varchar(1) default 'N'")
    private String notiYn;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(orphanRemoval=true,mappedBy = "user")
    private List<UserImage> userImages=new ArrayList<UserImage>();
    @OneToMany(orphanRemoval=true,mappedBy = "user")
    private List<UserInteresting> userInterestings=new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "chatRoomUser_id")
    private ChatRoomUser chatRoomUser;

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setLastaccesstime(LocalDateTime lastaccesstime) {
        this.lastaccesstime = lastaccesstime;
    }

    @Builder
    public User(SignUpForm signUpForm, String encoded) {
        this.userid=signUpForm.getUserid();
        this.nickname=signUpForm.getNickname();
        this.email=signUpForm.getEmail();
        this.gender=signUpForm.getGender();
        this.age=signUpForm.getAge();
        this.password = encoded;
        this.mbti = signUpForm.getMbti();
        this.major = signUpForm.getMajor();
        this.universeName = signUpForm.getUniverseName();
        this.universeCertiImg = signUpForm.getUniverseCertiImg();
        this.introduce = signUpForm.getIntroduce();
    }



}
