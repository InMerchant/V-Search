package com.mysite.sbb.channel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    List<Channel> findByUserNumber(int userNumber);
}
