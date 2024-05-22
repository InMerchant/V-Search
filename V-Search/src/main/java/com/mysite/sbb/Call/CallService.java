package com.mysite.sbb.Call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CallService {
	@Autowired
	private RestTemplate rT;

	public String sendCall(String url,Object requestPayload) {
		return rT.postForObject(url, requestPayload, String.class);
	}
}
