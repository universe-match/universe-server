package com.univer.universerver.source.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.univer.universerver.source.utils.DateAudit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private String groupkind;
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

    @OneToMany(orphanRemoval=true,mappedBy = "matchRoom")
    private List<Matching> matchingList=new ArrayList<Matching>();
    
}
