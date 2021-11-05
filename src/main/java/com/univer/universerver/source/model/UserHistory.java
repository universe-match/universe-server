package com.univer.universerver.source.model;

import com.univer.universerver.source.utils.DateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "user_history")
@Getter
@Setter
@NoArgsConstructor
public class UserHistory {

    @Id
    private String id;
    private String logindate;
    private String ostype;
    private String accessname;
    private String accesspath;
    private String ipaddress;
    private String sessionlastaccess;
    private long userNumber;
    private String userId;

}