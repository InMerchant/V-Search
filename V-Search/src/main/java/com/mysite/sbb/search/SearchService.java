package com.mysite.sbb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mysite.sbb.video.Video;
import com.mysite.sbb.video.VideoRepository;

import java.util.List;

@Service
public class SearchService {

	private final SearchRepository searchRepository;
	private final VideoRepository videoRepository; // VideoRepository 추가

	@Autowired
	public SearchService(SearchRepository searchRepository, VideoRepository videoRepository) {
		this.searchRepository = searchRepository;
		this.videoRepository = videoRepository; // VideoRepository 주입
	}

	public Page<Search> searchPage(String keyword, int page) {
		int pageSize = 10; // 페이지당 항목 수
		Page<Search> searchPage = searchRepository.findByVideoNameContaining(keyword, PageRequest.of(page, pageSize));

		// 검색 결과를 콘솔에 출력하고 비디오 URL을 가져오는 작업
		System.out.println("Search Page Result: " + searchPage.getContent());
		for (Search search : searchPage.getContent()) {
			int videoNumber = search.getVideoNumber();
			Video video = videoRepository.findByVideoNo(videoNumber);
			String videoUrl = video.getSTOURL(); // 비디오의 URL 가져오기
			// 여기에서 videoUrl을 사용하여 필요한 작업을 수행할 수 있습니다.
			System.out.println("Video URL for video number " + videoNumber + ": " + videoUrl);
		}

		return searchPage;
	}

	public List<Search> search(String keyword) {
		List<Search> searchResults = searchRepository.findByVideoNameContainingIgnoreCase(keyword);
		if (!searchResults.isEmpty()) {
			for (Search result : searchResults) {
				result.setVideoNumber(result.getVideoNumber());
			}

			return searchResults;
		}
		// 검색 결과가 비어 있는 경우에는 빈 리스트 반환
		return searchResults;
	}
}