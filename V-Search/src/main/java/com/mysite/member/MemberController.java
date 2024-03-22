package com.mysite.member;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.dao.DataIntegrityViolationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MemberController {

    private final MemberService userService;

    @GetMapping("/signup")
    public ModelAndView signupForm() {
        ModelAndView modelAndView = new ModelAndView("signup_form");
        modelAndView.addObject("userCreateForm", new MemberCreateForm());
        return modelAndView;
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form"; // 폼 유효성 검사 실패 시 다시 회원 가입 폼을 보여줍니다.
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form"; // 비밀번호 확인 실패 시 다시 회원 가입 폼을 보여줍니다.
        }

        try {
            userService.create(userCreateForm.getUsername(),  userCreateForm.getPassword1());
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form"; // 회원 가입 중 이미 등록된 사용자일 경우 다시 회원 가입 폼을 보여줍니다.
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form"; // 다른 예외 발생 시에도 다시 회원 가입 폼을 보여줍니다.
        }
        
        return "redirect:/"; // 회원 가입이 성공하면 홈 페이지로 리다이렉션합니다.
    }
    
    @GetMapping("/login")
    public String login() {
        return "login_form"; // 로그인 폼을 보여주는 View 파일의 이름을 반환합니다.
    }
}
