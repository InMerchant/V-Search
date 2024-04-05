package com.mysite.sbb.Withdrawal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @GetMapping("/withdraw")
    public String showWithdrawalPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return "withdrawal"; // 회원 탈퇴 페이지로 이동
        } else {
            // 사용자가 인증되지 않은 경우, 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
    }

    @PostMapping("/withdraw")
    public String withdrawMember(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 RedirectAttributes redirectAttributes) {
        boolean isWithdrawn = withdrawalService.withdrawMember(username, password);
        if (isWithdrawn) {
            redirectAttributes.addFlashAttribute("successMessage", "회원 탈퇴가 완료되었습니다.");
            return "redirect:/user/logout";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/userpage"; 
        }
    }
}
