package com.mysite.sbb.chat;


import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "VIDEO", schema = "admin")
@Data
public class ChatTitle {

	
	@Id
	@Column(name="VIDEO_NO")
	private int videoNo;
	
	@Column(name="VIDEO_NAME")
	private String videoname;
}


