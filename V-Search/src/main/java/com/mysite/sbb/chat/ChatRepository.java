package com.mysite.sbb.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    // 추가 쿼리 메서드가 필요한 경우 여기에 정의할 수 있습니다.
}
