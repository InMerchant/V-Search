package com.mysite.sbb.Call;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Calltest {
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
		return restTemplate;
	}
}
