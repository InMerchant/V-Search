package com.mysite.sbb.userpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserPageController {

    @Autowired
    private UserPageService userPageService;

    @GetMapping("/Member/{username}")
    public String getUserPage(@PathVariable("username") String username, Model model) {
        UserPage user = userPageService.getUserByUsername(username);
        if (user != null) {
            model.addAttribute("user", user);
            return "userpage"; // userpage.html을 렌더링하는 뷰의 이름
        } else {
            // 사용자를 찾을 수 없는 경우 처리
            return "userNotFound";
        }
    }

}
 
