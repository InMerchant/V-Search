package com.mysite.sbb.subscribe;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscribeApiController {
	@PostMapping("/api/subscribe/{toUserId}")
	public ResponseEntity<?> subscribe(@PathVariable int id){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return null;
	}
	
	@DeleteMapping("/api/subscribe/{toUserId}")
	public ResponseEntity<?> unsubscribe(@PathVariable int id){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return null;
	}
}
