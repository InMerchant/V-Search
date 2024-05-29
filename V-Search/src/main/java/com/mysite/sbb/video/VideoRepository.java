package com.mysite.sbb.video;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
	@Query("SELECT v.videoNo , v.VIDEO_NAME,v.USER_NO,v.STOURL FROM Video v")
	List<Object[]> findAllVideoNamesAndUserNumbersAndSTOURL();
	
	Video findByVideoNo(int videoNo);
	
	
}
