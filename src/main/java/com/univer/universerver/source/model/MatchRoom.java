package com.univer.universerver.source.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.univer.universerver.source.utils.DateAudit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matchroom")
@Getter
@Setter
@NoArgsConstructor
public class MatchRoom extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min=3, max = 50)
    @Column(name="title")
    private String title;
    @Size(min=3, max = 50)
    @Lob
    @Column(name="content")
    private String content;
    @Column(name="group_kind")
    private char groupkind;
    @Column(name="place")
    private String place;
    @Column(name="people_limit")
    private long peopleLimit;
    @Column(name="gender_kind")
    private String genderkind;
    @Column(name="from_date")
    private String fromDate;
    @Column(name="to_date")
    private String toDate;
    
    
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;
    
}
