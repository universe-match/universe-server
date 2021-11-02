package com.univer.universerver.source.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "common")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Common {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="com_cd")
    private String comCd;

}
