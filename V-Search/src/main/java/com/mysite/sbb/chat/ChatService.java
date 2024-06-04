package com.mysite.sbb.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public List<Map<String, String>> readDataFromDatabase() {
        List<Chat> chats = chatRepository.findAll();
        return chats.stream().map(chat -> {
            Map<String, String> map = new HashMap<>();
            map.put("FRAME_NUMBER", String.valueOf(chat.getFRAMENUMBER()));
            map.put("VIDEO_NO", String.valueOf(chat.getVideoNo()));
            map.put("CAPTION", chat.getCAPTION());
            map.put("SCRIPT", chat.getSCRIPT());
            map.put("TIMELINE", chat.getTIMELINE().toString());
            return map;
        }).collect(Collectors.toList());
    }
}
