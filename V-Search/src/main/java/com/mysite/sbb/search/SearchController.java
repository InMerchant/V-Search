package com.mysite.sbb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            Page<Search> searchPage = searchService.searchPage(keyword, page);
            if (!searchPage.isEmpty()) {
                model.addAttribute("searchPage", searchPage);
            } else {
                model.addAttribute("message", "검색 결과가 없습니다."); // 검색 결과가 없을 때 메시지 추가
            }
        }
        model.addAttribute("keyword", keyword); // 검색어를 모델에 추가
        return "search"; // search.html로 포워딩
    }
}
