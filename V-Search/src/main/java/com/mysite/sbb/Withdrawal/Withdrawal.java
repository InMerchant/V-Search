package com.mysite.sbb.Withdrawal;


import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.Data;

@Data
@Entity
@Table(name="MEMBER", schema = "bae")
public class Withdrawal {

	
    @Id
    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String pw;
}