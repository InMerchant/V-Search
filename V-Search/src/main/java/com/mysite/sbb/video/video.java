package com.mysite.sbb.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.mysite.sbb.recommend.Recommend;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Lob;// 파링 형식 blob 대용량 처리
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;
@Entity
@Table(name="VIDEO",schema = "bae")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class video {
	@Id
	@Column(name="VIDEO_NO")
	private int videoNo;
	
	private int USER_NO;
	@Lob
	private byte[] STORAGE;
	private String VIDEO_NAME;
	
	@OneToMany(mappedBy = "VIDEO", cascade = CascadeType.REMOVE)
    private List<Recommend> recommends;

    @Transient
    private boolean recommend_state;

    @Transient
    private int recommend_count;
}
