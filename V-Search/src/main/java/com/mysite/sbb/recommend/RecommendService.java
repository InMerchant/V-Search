package com.mysite.sbb.recommend;

import com.mysite.sbb.User.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class RecommendService {
    private final RecommendRepository recommendRepository;
    private final UserService userService;

    @Transactional
    public void toggleRecommendation(int videoNo) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int userNo = userService.getUserNO(username);

        Recommend existingRecommendation = recommendRepository.findByVideoNoAndUserNo(videoNo, userNo);
        if (existingRecommendation == null) {
            // 사용자가 아직 추천하지 않은 경우, 추천을 추가합니다.
            Recommend newRecommendation = new Recommend();
            newRecommendation.setVideoNo((long) videoNo); // videoNo를 Long 타입으로 설정
            newRecommendation.setUserNo(userNo); // userNo 설정
            newRecommendation.setRecommendCheck(true); // 추천 표시 설정
            recommendRepository.save(newRecommendation);
        } else {
            // 사용자가 이미 추천한 경우, 추천을 제거합니다.
            recommendRepository.delete(existingRecommendation);
        }
    }
    
    @Transactional(readOnly = true)
    public int getRecommendationsCountByVideoNo(int videoNo) {
        return recommendRepository.countByVideoNoAndRecommendCheck(videoNo, true);
    }
}