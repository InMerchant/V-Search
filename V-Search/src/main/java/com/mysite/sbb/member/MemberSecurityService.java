package com.mysite.sbb.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String ID) throws UsernameNotFoundException {
       
    	Optional<Member> _siteUser = this.memberRepository.findByID(ID);
        if (_siteUser.isEmpty()) {
  
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        Member siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(ID)) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
        }
        return new User(siteUser.getID(), siteUser.getPW(), authorities);
    }
}
