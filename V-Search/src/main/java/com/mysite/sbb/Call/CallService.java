package com.mysite.sbb.Call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.annotation.JsonProperty;

@Service
public class CallService {

	@Autowired
	private RestTemplate restTemplate;

	public String sendPostRequest(String url, String title) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		SomeRequestPayload payload=new SomeRequestPayload();
		payload.setTitle(title);
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

	public class SomeRequestPayload {
		@JsonProperty("title")
		private String title;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

}
