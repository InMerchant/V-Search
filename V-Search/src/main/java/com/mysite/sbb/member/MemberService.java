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

    public Member create(String ID, String PW) {
        Member user = new Member();
        user.setID(ID);
        user.setPW(PW); // 비밀번호를 해시화하지 않고 그대로 저장

        // 회원 저장
        this.MemberRepository.save(user);
        
        return user;
    }

    
    public Member getUser(String ID) {
        Optional<Member> Member = this.MemberRepository.findByID(ID);
        if (Member.isPresent()) {
            return Member.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}