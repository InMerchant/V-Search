package com.mysite.sbb.recommend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
@Data
@Entity
@Table(name = "RECOMMEND", schema = "bae")
public class Recommend {
   
	
    @Column(name = "VIDEO_NO")
    private int videoNo;
    
    
    @Column(name = "USER_NO")
    private int userNo; //

    @Column(name = "RECOMMEND_CHK")
    private boolean recommendCheck;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recommend_id_seq")
    @SequenceGenerator(name = "recommend_id_seq", sequenceName = "RECOMMEND_ID_SEQ", allocationSize = 1)
    @Column(name ="ID")
    private int id;
    
}

