package com.univer.universerver.source.model;

import com.univer.universerver.source.utils.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "auth_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userid;
}

