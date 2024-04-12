package com.mysite.sbb.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @GetMapping("/channel/{userNumber}")
    public String getVideosByUserNumber(@PathVariable("userNumber") int userNumber, Model model) {
        try {
            List<Channel> videos = channelService.getVideosByUserNumber(userNumber);
            if (videos.isEmpty()) {
                // Handle case when no videos found for the userNumber
                return "noVideosFound"; // You can replace "noVideosFound" with an appropriate view name
            }
            model.addAttribute("videos", videos);
            return "channel";
        } catch (Exception e) {
            // Handle case when userNumber is not provided in the request
            return "userNumberNotFound"; // You can replace "userNumberNotFound" with an appropriate view name
        }
    }

}