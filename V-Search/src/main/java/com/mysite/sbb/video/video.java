package com.mysite.sbb.video;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;// 파링 형식 blob 대용량 처리
import jakarta.persistence.Id;
@Entity
@Table(name="VIDEO",schema = "bae")
@Data
public class video {
	@Id
	private int VIDEO_NO;
	
	private int CREATOR_NO;
	@Lob
	private byte[] STORAGE;
	private String VIDEO_NAME;
	
}
