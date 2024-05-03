package com.mysite.sbb.comment;

import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.video.Video;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="comments", schema = "bae")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "userNo", referencedColumnName = "USER_NO")
    @ManyToOne
    private SiteUser user;

    @Column(name = "videoNo")
    private int videoNo; // 비디오 번호를 저장할 필드

    @Column(name = "content")
    private String content;
}
