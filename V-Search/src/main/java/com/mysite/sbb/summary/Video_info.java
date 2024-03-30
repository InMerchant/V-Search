package com.mysite.sbb.summary;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "VIDEO_INFO",schema = "bae")
@Data
public class Video_info {
	@Id
	private int FRAME_NO;
	private int VIDEO_NO;
	private String CAPTION;
	private String SCRIPT;
	private int CAPTION_ETHICS;
	private int SCRIPT_ETHICS;
	private String TIMELINE;
}
