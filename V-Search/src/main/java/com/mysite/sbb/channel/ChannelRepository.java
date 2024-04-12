package com.mysite.sbb.channel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {
    List<Channel> findByUserNumber(int userNumber);
}
