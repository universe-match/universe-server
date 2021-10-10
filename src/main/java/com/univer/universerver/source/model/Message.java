package com.univer.universerver.source.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Document(collection = "messages")
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private MessageType type;
    private Long chatroomId;
    private String sessionId;
    private long userKey;
    private String username;
    private String message;
    // private LocalDateTime sentAt;
    private String sentAt;

    private boolean checked;

    @Builder
    public Message(MessageType type, Long chatroomId, String sessionId,long userKey, String username, String message, String sentAt, boolean checked) {
        this.type = type;
        this.chatroomId = chatroomId;
        this.sessionId = sessionId;
        this.userKey = userKey;
        this.username = username;
        this.message = message;
        this.sentAt = sentAt;
        this.checked = checked;
    }
}
