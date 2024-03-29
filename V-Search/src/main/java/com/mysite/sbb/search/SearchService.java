package com.mysite.sbb.search;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

 private final SearchRepository searchRepository;

 @Autowired
 public SearchService(SearchRepository searchRepository) {
     this.searchRepository = searchRepository;
 }

 public List<Search> search(String keyword) {
     // 모든 동영상에서 keyword를 포함하는 것을 찾음
     return searchRepository.findByVideoNameContainingIgnoreCase(keyword);
 }
}
