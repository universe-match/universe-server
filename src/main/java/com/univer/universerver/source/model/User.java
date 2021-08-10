package com.univer.universerver.source.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.univer.universerver.source.model.request.SignUpForm;
import com.univer.universerver.source.utils.DateAudit;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private String nickname;

    @NaturalId
    @NotBlank
    private String userid;

    @Size(max = 50)
    @Email
    private String email;
    @JsonIgnore
    private String sex;
    @JsonIgnore
    private String age;


    private boolean verified = false;

    @NotBlank
    @Size(min=6, max = 100)
    @JsonIgnore
    private String password;

    @Transient
    private String password2;

    @Lob
    private String introduce;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name="reportedCnt")
    @ColumnDefault("0")
    private long reportedCnt;

    @Column(name="account_suspend")
    @ColumnDefault("0")
    private long accountSuspend;

    private String fcmToken;

    @Column(name="last_access_time")
    private LocalDateTime lastaccesstime;

    @Column(name="alarm_check")
    @ColumnDefault("0")
    private boolean alarmCheck;

    @Column(name="point")
    @ColumnDefault("0")
    private long point;


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
        this.sex=signUpForm.getSex();
        this.age=signUpForm.getAge();
        this.password = encoded;
    }



}
