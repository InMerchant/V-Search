package com.mysite.sbb.recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
	@Modifying
    @Query(value = "INSERT INTO recommend(VIDEO_NO, USER_NO) VALUES(:video_no, :user_no)", nativeQuery = true)
    int recommend(Long video_no, Long user_no);

    @Modifying
    @Query(value = "DELETE FROM recommend WHERE VIDEO_NO = :video_no AND USER_NO = :user_no", nativeQuery = true)
    int cancelRecommend(Long video_no, Long user_no);
}
