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
        // 모든 동영상에서 keyword를 포함하는 것을 찾음
        List<Search> searchResults = searchRepository.findByVideoNameContainingIgnoreCase(keyword);
        // 검색 결과가 비어 있지 않다면
        if (!searchResults.isEmpty()) {
            // 각 검색 결과의 userNumber를 가져와서 설정
            for (Search result : searchResults) {
                result.setVideoNumber(result.getVideoNumber()); // 각 검색 결과의 videoNumber를 가져와 설정
            }
            System.out.println(searchResults);
            return searchResults;
        }
        // 검색 결과가 비어 있는 경우에는 빈 리스트 반환
        return searchResults;
    }
}
