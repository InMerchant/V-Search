package com.mysite.sbb.subscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mysite.sbb.User.UserService;
import com.mysite.sbb.delvideo.Del;

import jakarta.persistence.EntityManager;

@RestController
public class SubscribeApiController {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private UserService userService;
	@Autowired
	private SubscribeService subscribeService;

	@PostMapping("/api/subscribe/{videoNo}")
	public ResponseEntity<?> subscribe(@PathVariable("videoNo") int videoNo) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		int principalNo = userService.getUserNO(username);
		Del video = entityManager.find(Del.class, videoNo);
		int videoUserId=video.getUsernumber();//금요일 클래스화 잊지 말것!
		subscribeService.subscribe(principalNo, videoUserId);
		
		return null;
	}

	@GetMapping("/api/subscribe/{videoNo}")
	public ResponseEntity<?> unsubscribe(@PathVariable("videoNo") int videoNo) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		int principalNo = userService.getUserNO(username);
		Del video = entityManager.find(Del.class, videoNo);
		int videoUserId=video.getUsernumber();
		subscribeService.unSubscribe(principalNo, videoUserId);
		return null;
	}
}
