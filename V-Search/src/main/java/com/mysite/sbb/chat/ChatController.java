package com.mysite.sbb.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/asd")
    public String showChatFormPage(Model model) {
        // 데이터베이스에서 데이터를 가져와 모델에 추가
        List<Map<String, String>> data = chatService.readDataFromDatabase();
        model.addAttribute("data", data);
        return "chat";
    }

    @PostMapping("/asd")
    @ResponseBody
    public ResponseEntity<Map<String, String>> submitFormData(@RequestBody Map<String, String> requestData) {
        System.out.println("Received data from client: " + requestData);

        // 데이터베이스에서 데이터를 가져오기
        List<Map<String, String>> data = chatService.readDataFromDatabase();

        // 사용자 요청 데이터와 데이터베이스에서 가져온 데이터를 합침
        requestData.put("db_data", data.toString());  // 혹은 필요한 형식으로 변환

        // Python 서버로 데이터 전송
        String pythonServerUrl = "http://localhost:5000/receive_data";
        ResponseEntity<Map> response = restTemplate.postForEntity(pythonServerUrl, requestData, Map.class);

        return ResponseEntity.ok().body(response.getBody());
    }
}
