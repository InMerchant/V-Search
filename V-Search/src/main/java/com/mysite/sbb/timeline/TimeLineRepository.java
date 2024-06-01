package com.mysite.sbb.timeline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TimeLineRepository extends JpaRepository<TimeLine, Integer> {
	List<TimeLine> findByvideoNoOrderByFRAMENUMBERAsc(int videoNO);
}
