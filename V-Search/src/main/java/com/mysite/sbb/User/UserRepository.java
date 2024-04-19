package com.mysite.sbb.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Integer> {
    Optional<SiteUser> findByusername(String username);
    
    Optional<SiteUser> findByUserNo(Integer userNo);
}