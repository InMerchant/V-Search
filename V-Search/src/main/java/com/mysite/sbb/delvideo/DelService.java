package com.mysite.sbb.delvideo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.mysite.sbb.User.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class DelService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Transactional
    public boolean deleteVideo(int videoNo) {
        // 현재 로그인한 사용자의 아이디를 가져옴
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 사용자 아이디를 기반으로 사용자 번호를 조회
        int userNo = userService.getUserNO(username);

        // 주어진 동영상 번호에 해당하는 동영상을 데이터베이스에서 조회
        Del video = entityManager.find(Del.class, videoNo);

        // 동영상이 존재하고, 현재 로그인한 사용자가 동영상의 소유자인 경우
        if (video != null && video.getUsernumber() == userNo) {
            // 동영상 삭제
            entityManager.remove(video);
            return true; // 삭제 성공
        } else {
            return false; // 삭제 실패
        }
    }
}
