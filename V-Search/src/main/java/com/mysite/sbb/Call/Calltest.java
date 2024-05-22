package com.mysite.sbb.Call;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Calltest {
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(
				Arrays.asList(new MappingJackson2HttpMessageConverter(), new StringHttpMessageConverter() // 이 줄을 추가합니다.
				));
		return restTemplate;
	}
}
