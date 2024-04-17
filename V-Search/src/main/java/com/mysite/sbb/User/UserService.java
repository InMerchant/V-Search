package com.mysite.sbb.User;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

import com.mysite.sbb.CustomApiException;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.subscribe.SubscribeRepository;
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscribeRepository subscribeRepository;

    public SiteUser create(String username, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
    
    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

	public int getUserNO(String username) {
		Optional<SiteUser> userOptional = userRepository.findByusername(username);
		SiteUser user = userOptional.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
		return user.getUSER_NO();
	}
	
	@Transactional(readOnly = true)
	public UserProfileDto userProfile(int videoUserId) {
		UserProfileDto dto = new UserProfileDto();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		int principalId = getUserNO(username);
		
		//문제생길확률 있음
		SiteUser userEntity = userRepository.findById(username).orElseThrow(()-> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});
		
		dto.setUser(userEntity);
		dto.setVideoOwnerState(videoUserId == principalId);
		
		int subscribeState = subscribeRepository.mSubscribeState(principalId, videoUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(videoUserId);
		
		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);
		
		return dto;
	}
}
