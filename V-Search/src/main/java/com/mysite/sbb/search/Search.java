package com.mysite.sbb.search;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.Data;

@Data
@Entity
@Table(name="VIDEO", schema = "bae")
public class Search {
    @Id
    @Column(name = "VIDEO_NAME")
    private String videoName;

    @Column(name = "USER_NO")
    private int userNumber;
    
    
}