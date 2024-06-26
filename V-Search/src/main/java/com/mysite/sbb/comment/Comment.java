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
@Table(name="comments", schema = "admin")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "userNo", referencedColumnName = "USER_NO")
    @ManyToOne
    private SiteUser user;

    @JoinColumn(name = "videoNo", referencedColumnName = "VIDEO_NO")
    @ManyToOne
    private Video video;

    @Column(name = "content")
    private String content;
}
