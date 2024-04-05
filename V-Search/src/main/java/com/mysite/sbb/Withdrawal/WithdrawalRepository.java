package com.mysite.sbb.Withdrawal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, String> {
    Withdrawal findByUserName(String username);
}
