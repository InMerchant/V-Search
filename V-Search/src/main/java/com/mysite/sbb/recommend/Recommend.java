package com.mysite.sbb.recommend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "RECOMMEND", schema = "bae")
public class Recommend {
   
    @Column(name = "VIDEO_NO")
    private int videoNo;
    
    @Id
    @Column(name = "USER_NO")
    private int userNo; //

    @Column(name = "RECOMMEND_CHK")
    private boolean recommendCheck;
}
