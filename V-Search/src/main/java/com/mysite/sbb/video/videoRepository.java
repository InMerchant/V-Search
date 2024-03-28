package com.mysite.sbb.video;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface videoRepository extends JpaRepository<video, Integer>{
	   @Query("SELECT v.VIDEO_NO , v.VIDEO_NAME,v.USER_NO FROM video v")
	   List<Object[]> findAllVideoNamesAndUserNumbers();
}

	