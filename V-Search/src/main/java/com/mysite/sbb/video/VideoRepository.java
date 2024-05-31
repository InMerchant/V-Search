package com.mysite.sbb.video;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.sbb.search.Search;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
	@Query("SELECT v.videoNo , v.VIDEO_NAME,v.USER_NO,v.STOURL FROM Video v")
	List<Object[]> findAllVideoNamesAndUserNumbersAndSTOURL();

	Video findByVideoNo(int videoNo);

	@Query("SELECT v FROM Video v WHERE v.USER_NO = :userNo AND v.VIDEO_NAME = :title")
	Video findByUserNoAndTitle(@Param("userNo") int userNo, @Param("title") String title);

}
