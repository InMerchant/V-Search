package com.mysite.sbb.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (videos.isEmpty()) {
                return "noVideosFound"; 
            }
            model.addAttribute("username", username);
            model.addAttribute("videos", videos);
            model.addAttribute("userNo",userNumber);
            return "channel";
        } catch (Exception e) {
            return "userNumberNotFound"; // You can replace "userNumberNotFound" with an appropriate view name
        }
    }

}
