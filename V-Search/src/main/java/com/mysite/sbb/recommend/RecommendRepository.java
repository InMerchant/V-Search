package com.mysite.sbb.recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
	@Modifying
    @Query(value = "INSERT INTO RECOMMEND(VIDEO_NO, USER_NO) VALUES(:videoNo, :userNo)", nativeQuery = true)
	int recommend(@Param("videoNo") Long videoNo, @Param("userNo") int userNo);

    @Modifying
    @Query(value = "DELETE FROM recommend WHERE VIDEO_NO = :videoNo AND USER_NO = :userNo", nativeQuery = true)
    int cancelRecommend(@Param("videoNo") Long videoNo, @Param("userNo") int userNo);
}
