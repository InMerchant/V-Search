package com.mysite.sbb.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<SiteUser, Integer> {
    Optional<SiteUser> findByusername(String username);
    
    Optional<SiteUser> findByUserNo(Integer userNo);
    
    @Query("SELECT username FROM SiteUser WHERE userNo=:videoNo")
    String findByuserno(Integer videoNo);
    
}