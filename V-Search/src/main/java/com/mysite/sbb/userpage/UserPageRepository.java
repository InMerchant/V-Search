package com.mysite.sbb.userpage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPageRepository extends JpaRepository<UserPage, String> {

    UserPage findByUsername(String username);
}
