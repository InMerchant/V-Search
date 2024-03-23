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

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String ID,String PW) {
        Member user = new Member();
        user.setID(ID);
        user.setPW(passwordEncoder.encode(PW));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPW(passwordEncoder.encode(PW));
        this.memberRepository.save(user);
        return user;
    }
    
    public Member getUser(String ID) {
        Optional<Member> member = this.memberRepository.findByID(ID);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
