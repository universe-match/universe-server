package com.univer.universerver.source.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.univer.universerver.source.model.request.SignUpForm;
import com.univer.universerver.source.utils.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    
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
    }



}
