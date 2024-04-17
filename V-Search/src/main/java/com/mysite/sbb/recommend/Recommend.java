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
    @Id
    @Column(name = "VIDEO_NO")
    private Long videoNo;

    @Column(name = "USER_NO")
    private int userNo; // userNo는 문자열로 설정합니다.

    @Column(name = "RECOMMEND_CHK")
    private boolean recommendCheck;
}
