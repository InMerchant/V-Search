package com.mysite.sbb.member;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Table(name="MEMBER",schema = "bae")

public class Member {
	@Id
	    @Column(unique = true, nullable = false)
	    private String ID;
	    private String PW;
}
