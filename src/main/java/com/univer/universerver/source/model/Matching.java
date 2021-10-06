package com.univer.universerver.source.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.univer.universerver.source.utils.DateAudit;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "matching")
@Getter
@Setter
public class Matching extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "matchroom_id")
	private MatchRoom matchRoom;
	
    @Column(name="agree")
    @ColumnDefault("N") 
    private char agree;

    @Column(name="master_yn")
    @ColumnDefault("N")
    private char masterYn;
}
