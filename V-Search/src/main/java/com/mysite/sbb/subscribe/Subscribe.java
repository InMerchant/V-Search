package com.mysite.sbb.subscribe;

import java.time.LocalDateTime;

import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.video.Video;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Subscribe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	//구독한사람
	@JoinColumn(name = "fromUserId", referencedColumnName = "USER_NO")
	@ManyToOne
	private SiteUser fromUser;
	
	//구독받는사람
	@JoinColumn(name = "toUserId", referencedColumnName = "USER_NO")
	@ManyToOne
	private SiteUser toUser;
	
	@JoinColumn(name = "videoNo", referencedColumnName = "VIDEO_NO")
    @ManyToOne
    private Video video;
	
	private LocalDateTime createDate;
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
	
}

