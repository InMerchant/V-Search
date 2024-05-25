package com.mysite.sbb.timeline;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TimeLineService {
	@Autowired
	private TimeLineRepository TR;

	public List<TimeLine> timeLineCaption(int videoNO) {
		List<TimeLine> timeLineList =TR.findByvideoNo(videoNO);
		return timeLineList;
	}
}
