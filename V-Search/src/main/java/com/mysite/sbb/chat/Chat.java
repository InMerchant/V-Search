package com.mysite.sbb.chat;


import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "VIDEO_INFO", schema = "admin")
@Data
public class Chat {

	
	@Id
	@Column(name="FRAME_NUMBER")
	private int FRAMENUMBER;
	@Column(name="VIDEO_NO")
	private int videoNo;
	
	@Column(name="CAPTION")
	private String CAPTION;
	@Column(name="TIMELINE")
	private LocalDateTime TIMELINE;
	@Column(name="SCRIPT")
	private String SCRIPT;
}


