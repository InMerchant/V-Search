package com.mysite.sbb.timeline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeLineRepository extends JpaRepository<TimeLine, Integer> {
	List<TimeLine> findByvideoNo(int videoNO);
}
