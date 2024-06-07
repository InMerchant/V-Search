package com.mysite.sbb.agechk;

import java.time.LocalDateTime;

import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.subscribe.Subscribe;
import com.mysite.sbb.video.Video;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "VIDEO_INFO", schema = "bae")
public class VideoChk {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int infoId;

	@Column(name = "SCRIPTIMO")
	private int scriptimo;

	@Column(name = "VIDEO_NO")
	private int videoNo;

	@Column(name = "NUDE")
	private int nude;

}
