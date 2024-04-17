package com.mysite.sbb.User;


import java.util.List;

import com.mysite.sbb.subscribe.Subscribe;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
@Table(name="MEMBER",schema = "bae")
public class SiteUser {
	@Id
	@Column(unique = true, nullable = false)
	private String username;
	private String password;


	// 외래 키 매핑 추가
    // fromUserId와 toUserId 모두 SiteUser 테이블의 username을 참조하도록 설정
    @OneToMany(mappedBy = "fromUser")
    private List<Subscribe> fromSubscriptions;

    @OneToMany(mappedBy = "toUser")
    private List<Subscribe> toSubscriptions;

    // USER_NO 데이터 유형을 변경하여 데이터베이스와 일치시킴
    @Column(name = "USER_NO")
    private int USER_NO;
}
