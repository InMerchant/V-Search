package com.mysite.sbb.timeline;

import java.time.LocalDateTime;

import com.mysite.sbb.video.Video;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "VIDEO_INFO", schema = "admin")
@Data
public class TimeLine {
	@Id
	private int infoId;
	@Column(name="FRAME_NUMBER")
	private int FRAMENUMBER;
	@Column(name="VIDEO_NO")
	private int videoNo;
	private String CAPTION;
	private LocalDateTime TIMELINE;
}
