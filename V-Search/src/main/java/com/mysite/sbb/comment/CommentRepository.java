package com.mysite.sbb.comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // 추가적인 메서드가 필요한 경우 여기에 작성할 수 있습니다.
}
