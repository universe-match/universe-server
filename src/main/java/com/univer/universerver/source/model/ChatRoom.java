package com.univer.universerver.source.model;

import com.univer.universerver.source.utils.DateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "user_id")
//    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "matchroom_id")
    private MatchRoom matchRoom;
}
