package com.mysite.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import com.mysite.sbb.DataNotFoundException;
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String email, String password) {
    	Member user = new Member();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
    
    public Member getUser(String username) {
        Optional<Member> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("Member not found");
        }
    }
}
