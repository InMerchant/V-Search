package com.mysite.sbb.Call;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class CallService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	public String sendPostRequest(String url, String folderName) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String payloadJson;
		payloadJson = "{\"folder_name\":\"" + folderName + "\"}";
		System.out.println(payloadJson);
		HttpEntity<String> requestEntity = new HttpEntity<>(payloadJson, headers);
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					String.class);
			return responseEntity.getBody();
		} catch (RestClientException e) {
			e.printStackTrace();
			return "Request failed: " + e.getMessage();
		}
	}

	public class SomeRequestPayload {
		private String folder_Name;

		// ObjectMapper가 이스케이핑할 수 있도록 getter 메서드 이름을 변경합니다.
		@JsonProperty("folder_Name")
		public String getFolderName() {
			return folder_Name;
		}

		public void setFolderName(String folder_Name) {
			this.folder_Name = folder_Name;
		}
	}

}
