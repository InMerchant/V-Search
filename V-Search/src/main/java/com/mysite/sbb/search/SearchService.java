package com.mysite.sbb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

	private final SearchRepository searchRepository;

	@Autowired
	public SearchService(SearchRepository searchRepository) {
		this.searchRepository = searchRepository;
	}

	public Page<Search> searchPage(String keyword, int page) {
		int pageSize = 10; // 페이지당 항목 수
		return searchRepository.findByVideoNameContaining(keyword, PageRequest.of(page, pageSize));
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
