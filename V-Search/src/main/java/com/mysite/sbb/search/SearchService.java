package com.mysite.sbb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchService {

    private final SearchRepository searchRepository;

    @Autowired
    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public List<Search> search(String keyword) {
        // 모든 동영상에서 keyword를 포함하는 것을 찾음
        List<Search> searchResults = searchRepository.findByVideoNameContainingIgnoreCase(keyword);
        // 검색 결과가 비어 있지 않다면
        if (!searchResults.isEmpty()) {
            // 각 검색 결과의 userNumber를 가져와서 설정
            for (Search result : searchResults) {
                result.setUserNumber(searchResults.get(0).getUserNumber());
            }
        }
        return searchResults;
    }
}
