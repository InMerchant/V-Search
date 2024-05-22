package com.mysite.sbb.Call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CallController implements CommandLineRunner {

	@Autowired
	private CallService callService;

	public static void main(String[] args) {
		SpringApplication.run(CallController.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String url = "http://192.168.34.28:5000/flaskTest";
		Object requestPayload = new SomeRequestPayload();
		try {
			String postResponse = callService.sendCall(url, requestPayload);
			System.out.println("POST Response: " + postResponse);
		} catch (Exception e) {
			System.err.println("POST 요청 오류: " + e.getMessage());
		}
	}

	class SomeRequestPayload {
		private String field1;
		private int field2;

		// getters and setters

		public SomeRequestPayload() {
			this.field1 = "value1";
			this.field2 = 123;
		}
	}
}
