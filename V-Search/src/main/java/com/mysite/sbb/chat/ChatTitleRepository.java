package com.mysite.sbb.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatTitleRepository extends JpaRepository<ChatTitle, Integer> {
    // 추가 쿼리 메서드가 필요한 경우 여기에 정의할 수 있습니다.
}
