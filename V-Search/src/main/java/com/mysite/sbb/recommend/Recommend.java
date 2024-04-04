package com.mysite.sbb.recommend;

import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.video.video;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="RECOMMEND",schema = "bae")
public class Recommend {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JoinColumn(name = "VIDEO_NO")
	@ManyToOne(fetch = FetchType.LAZY)
	private video VIDEO;
	
	@JoinColumn(name = "USER_NO")
	@ManyToOne(fetch = FetchType.LAZY)
	private SiteUser user;
}
