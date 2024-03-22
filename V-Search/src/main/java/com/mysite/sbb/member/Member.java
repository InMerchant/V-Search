package com.mysite.sbb.member;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Table(name="VIEWER",schema = "bae")

public class Member {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(unique = true)
	    private String USERID;
	    private String PW;
}
