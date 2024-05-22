package com.mysite.sbb.Call;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.FileOutputStream;

@Service
public class CallService {

	@Autowired
	private RestTemplate restTemplate;

	public String sendPostRequest(String url, SomeRequestPayload requestPayload) {
		ResponseEntity<String> response = restTemplate.postForEntity(url, requestPayload, String.class);
		return response.getBody();
	}

	public void downloadFile(String url, String file) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// HTTP 요청 전송
		ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class);

		// 응답 본문을 파일로 저장
		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(responseEntity.getBody());
		outputStream.close();
	}
}

class SomeRequestPayload {
	private String field1;
	private int field2;

	// 기본 생성자, getters, setters

	public SomeRequestPayload() {
		this.field1 = "value1";
		this.field2 = 123;
	}

	// getters and setters
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public int getField2() {
		return field2;
	}

	public void setField2(int field2) {
		this.field2 = field2;
	}
}
