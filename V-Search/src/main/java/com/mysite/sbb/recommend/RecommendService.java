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
        // 현재 로그인한 사용자 이름 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 현재 로그인한 사용자 번호 가져오기
        int userNo = userService.getUserNO(username);

        // 해당 비디오에 대한 사용자의 추천 찾기
        Recommend existingRecommendation = recommendRepository.findByVideoNoAndUserNo(videoNo, userNo);
        if (existingRecommendation == null) {
            // 해당 비디오에 대한 사용자의 추천이 없는 경우, 새로운 추천 추가
            Recommend newRecommendation = new Recommend();
            newRecommendation.setVideoNo(videoNo);
            newRecommendation.setUserNo(userNo);
            newRecommendation.setRecommendCheck(true);
            recommendRepository.save(newRecommendation);
        } else {
            // 해당 비디오에 대한 사용자의 추천이 있는 경우, 추천 상태 변경
            existingRecommendation.setRecommendCheck(!existingRecommendation.isRecommendCheck());
            recommendRepository.save(existingRecommendation);
        }
    }


    @Transactional(readOnly = true)
    public int getRecommendationsCountByVideoNo(int videoNo) {
        // 해당 비디오의 추천 수 계산
        return recommendRepository.countByVideoNoAndRecommendCheck(videoNo, true);
    }

    @Transactional(readOnly = true)
    public boolean hasUserRecommended(int videoNo) {
        // 현재 로그인한 사용자 이름 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 현재 로그인한 사용자 번호 가져오기
        int userNo = userService.getUserNO(username);
        // 현재 로그인한 사용자가 특정 비디오를 추천했는지 확인
        Recommend existingRecommendation = recommendRepository.findByVideoNoAndUserNo(videoNo, userNo);
        return existingRecommendation != null;
    }
}
