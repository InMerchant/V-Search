package com.mysite.sbb.User;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
		return user.getUserNo();
	}
	
	@Transactional(readOnly = true)
	public UserProfileDto userProfile(int videoUserId, int videoNo) {
		
		UserProfileDto dto = new UserProfileDto();
		
		//로그인한 유저 이름
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		//로그인한 유저 아이디
		int principalId;
		
		//구독여부
		int subscribeState;
		
		//비디오 구독수
		int subscribeCount;
		
		//로그인한 유저 객체
		SiteUser userEntity;
		
		subscribeCount = subscribeRepository.mSubscribeCount(videoNo);
		dto.setSubscribeCount(subscribeCount);
		
		// 현재 로그인 여부 체크
		if (username == "anonymousUser") {
			dto.setSubscribeState(false);
	        return dto;
	    }
		else {
			principalId = getUserNO(username);	
			
			userEntity = userRepository.findByUserNo(principalId).orElseThrow(()-> {
				throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
			});
			
			dto.setUser(userEntity);
			dto.setVideoOwnerState(videoUserId == principalId);
			
			subscribeState = subscribeRepository.mSubscribeState(principalId, videoUserId);
			
			
			dto.setSubscribeState(subscribeState == 1);
			
			
			return dto;
		}
	}
}
