package com.mysite.sbb.Withdrawal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean withdrawMember(String username) {
        Withdrawal withdrawal = withdrawalRepository.findByUserName(username);
        if (withdrawal != null) {
            withdrawalRepository.delete(withdrawal);
            return true; // 회원 탈퇴 성공
        } else {
            return false; // 회원 탈퇴 실패
        }
    }
}
