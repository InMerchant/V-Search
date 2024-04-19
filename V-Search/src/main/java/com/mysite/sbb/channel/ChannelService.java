package com.mysite.sbb.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;

    public List<Channel> getVideosByUserNumber(int userNumber) {
        return channelRepository.findByUserNumber(userNumber);
    }
    
}
