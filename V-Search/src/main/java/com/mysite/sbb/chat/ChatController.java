package com.mysite.sbb.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ChatService chatService;

    @GetMapping("/asd")
    public String showChatFormPage(Model model) {
        return "chat";
    }

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/asd")
    @ResponseBody
    public ResponseEntity<Map<String, String>> submitFormData(@RequestBody Map<String, String> requestData) {
        System.out.println("Received data from client: " + requestData);

        // 데이터베이스에서 데이터 읽기
        List<Map<String, String>> dbData = chatService.readDataFromDatabase();
        List<Map<String, String>> dbDataTitle = chatService.readDataFromDatabasetitle();

        try {
            // dbData를 JSON 문자열로 변환
            String dbDataJson = objectMapper.writeValueAsString(dbData);
            String dbDataTitleJson = objectMapper.writeValueAsString(dbDataTitle);
            
            // 요청 데이터에 데이터베이스 데이터를 추가
            requestData.put("db_data", dbDataJson);
            requestData.put("db_data_title", dbDataTitleJson);

            String pythonServerUrl = "http://localhost:5000/receive_data";
            ResponseEntity<Map> response = restTemplate.postForEntity(pythonServerUrl, requestData, Map.class);

            return ResponseEntity.ok().body(response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
