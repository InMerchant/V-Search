package com.mysite.sbb.Call;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.annotation.JsonProperty;

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

	public String sendPostvideoNo(String url, int videoNo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String payloadJson;
		payloadJson = "{\"Video_NO\":\"" + videoNo + "\"}";
		HttpEntity<Integer> requestEntity = new HttpEntity<>(videoNo, headers);
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					String.class);
			return responseEntity.getBody();
		} catch (RestClientException e) {
			e.printStackTrace();
			return "Request failed: " + e.getMessage();
		}
	}

}
