package com.mysite.sbb.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import com.mysite.sbb.DataNotFoundException;
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository MemberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String USERID,String PW) {
    	Member user = new Member();
        user.setUSERID(USERID);
        user.setPW(passwordEncoder.encode(PW));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPW(passwordEncoder.encode(PW));
        this.MemberRepository.save(user);
        return user;
    }
    
    public Member getUser(String USERID) {
        Optional<Member> Member = this.MemberRepository.findByUSERID(USERID);
        if (Member.isPresent()) {
            return Member.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}