package com.mysite.sbb.Call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CallController implements CommandLineRunner {

	@Autowired
	private CallService callService;

	public static void main(String[] args) {
		SpringApplication.run(CallController.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String url = "http://192.168.34.28:5000/flaskTest";
		SomeRequestPayload requestPayload = new SomeRequestPayload(); // 요청에 사용할 데이터 객체
		try {
			/*
			 * String postResponse = callService.sendPostRequest(url, requestPayload);
			 * System.out.println("POST Response: " + postResponse);
			 */

			String download = url;
			String fileName = "test.json";
			callService.downloadFile(download, fileName);
		} catch (Exception e) {
			System.err.println("POST 요청 오류: " + e.getMessage());
		}
	}
}
