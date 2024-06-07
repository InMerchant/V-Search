package com.mysite.sbb.agechk;

import java.time.LocalDateTime;

import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.subscribe.Subscribe;
import com.mysite.sbb.video.Video;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	
	@Column(name = "SCRIPTIMO")
    private int scriptimo;
    
	@Column(name = "NUDE")
    private int nude;
    
}
