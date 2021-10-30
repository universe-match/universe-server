package com.univer.universerver.source.model;

import com.univer.universerver.source.utils.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Complain extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(name="content")
    private String content;

    @Column(name="userkey")
    private long userKey;
}
