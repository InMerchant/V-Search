package com.mysite.sbb.channel;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@Table(name="VIDEO", schema = "bae")
public class Channel {
    @Id
    @Column(name = "VIDEO_NAME")
    private String videoName;

    @Column(name = "VIDEO_NO")
    private int videoNumber;
    
    
}