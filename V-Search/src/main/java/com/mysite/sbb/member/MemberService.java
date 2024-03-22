package com.mysite.sbb.member;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import com.mysite.common.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String password) {
        Member user = new Member();
        user.setUSERID(username);
        user.setPW(passwordEncoder.encode(password)); // 주입받은 passwordEncoder 빈을 사용하여 패스워드 암호화
        this.memberRepository.save(user);
        return user;
    }
    
    public Member getUser(String username) {
        Optional<Member> member = this.memberRepository.findByUSERID(username);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("Member not found");
        }
    }
}
