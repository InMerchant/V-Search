package com.mysite.sbb.channel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.sbb.video.video;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {
    List<Channel> findByUserNumber(int userNumber);
}
