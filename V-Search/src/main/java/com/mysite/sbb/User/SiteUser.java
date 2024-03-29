package com.mysite.sbb.User;


import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Table(name="MEMBER",schema = "bae")
public class SiteUser {
	@Id
	@Column(unique = true, nullable = false)
	private String username;
	private String password;
	private int USER_NO;
}
