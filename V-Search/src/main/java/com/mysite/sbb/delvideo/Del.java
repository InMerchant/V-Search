package com.mysite.sbb.delvideo;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name="VIDEO",schema = "bae")
@Data

public class Del {
    @Id
    @Column(name="VIDEO_NO")
    private int videoNo;
    
    @Column(name="USER_NO")
    private int usernumber; // 
   
}
