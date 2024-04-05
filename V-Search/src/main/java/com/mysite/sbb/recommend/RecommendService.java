package com.mysite.sbb.recommend;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecommendService {
	private final RecommendRepository recommendRepository;

	@Transactional
    public void recommend(long video_no, int user_no) {
		System.out.println(video_no+" "+user_no);
        recommendRepository.recommend(video_no, user_no);
    }

    @Transactional
    public void cancelRecommend(Long video_no, int user_no) {
        recommendRepository.cancelRecommend(video_no, user_no);
    }
}
