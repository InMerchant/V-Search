package com.mysite.sbb; 

import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.ResponseBody; 

@Controller 
public class HelloController { 
    @GetMapping("/test") 
    public String hello() { 
        return "signup_form"; 
    } 
}