package com.mysite.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import com.mysite.common.*;
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository MemberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String password) {
    	Member user = new Member();
        user.setUSERID(username);
        user.setPW(passwordEncoder.encode(password));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPW(passwordEncoder.encode(password));
        this.MemberRepository.save(user);
        return user;
    }
    
    public Member getUser(String username) {
        Optional<Member> member = this.MemberRepository.findByusername(username);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("Member not found");
        }
    }
}
