package com.mysite.sbb.userpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPageService {

    @Autowired
    private UserPageRepository userPageRepository;

    public UserPage getUserByUsername(String username) {
        return userPageRepository.findByUsername(username);
    }
}
