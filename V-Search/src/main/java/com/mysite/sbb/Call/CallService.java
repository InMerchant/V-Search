package com.mysite.sbb.Call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.mysite.sbb.Call.SomeRequestPayload;

@Service
public class CallService {

	@Autowired
	private RestTemplate restTemplate;

	public String sendPostRequest(String url, SomeRequestPayload payload) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<SomeRequestPayload> requestEntity = new HttpEntity<>(payload, headers);

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
