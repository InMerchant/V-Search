package com.mysite.sbb.Withdrawal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @GetMapping("/withdraw") // GET 요청 처리
    public String showWithdrawalPage() {
        return "withdrawal"; // 회원 탈퇴 페이지로 이동
    }

    @PostMapping("/withdraw")
    public String withdrawMember(RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // 현재 사용자의 이름 가져오기

        boolean isWithdrawn = withdrawalService.withdrawMember(username);
        if (isWithdrawn) {
            redirectAttributes.addFlashAttribute("successMessage", "회원 탈퇴가 완료되었습니다.");
            return "redirect:/"; // 로그아웃 페이지로 리다이렉트
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/userpage"; // 사용자 페이지로 리다이렉트
        }
    }
}
