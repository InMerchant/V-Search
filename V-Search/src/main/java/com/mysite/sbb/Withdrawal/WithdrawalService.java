package com.mysite.sbb.Withdrawal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public boolean withdrawMember(String username, String password) {
        // 사용자명을 이용하여 회원 정보를 가져옵니다.
        Withdrawal withdrawal = withdrawalRepository.findByUserName(username);

        // 회원 정보가 존재하는지 확인합니다.
        if (withdrawal == null) {
            // 회원 정보가 없을 경우 탈퇴 실패를 반환합니다.
            return false;
        }

        // 입력된 비밀번호와 저장된 비밀번호를 비교하여 일치하는지 확인합니다.
        if (passwordEncoder.matches(password, withdrawal.getPassword())) {
            // 비밀번호가 일치하면 회원 탈퇴를 수행합니다.
            withdrawalRepository.delete(withdrawal);
            return true; // 회원 탈퇴 성공
        } else {
            // 비밀번호가 일치하지 않을 경우 탈퇴 실패를 반환합니다.
            return false;
        }
    }
}
