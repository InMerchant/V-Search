package com.mysite.sbb.member;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.dao.DataIntegrityViolationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup(MemberCreateForm form) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!form.getPW1().equals(form.getPW2())) {
            bindingResult.rejectValue("PW2", "PWInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            memberService.create(form.getUSERID(), form.getPW1());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}

