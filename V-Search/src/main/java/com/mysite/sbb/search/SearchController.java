package com.mysite.sbb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        if(keyword != null && !keyword.isEmpty()) {
            List<Search> searchResults = searchService.search(keyword);
            model.addAttribute("searchResults", searchResults);
            // userNumber 정보도 함께 모델에 추가
            model.addAttribute("userNumber", searchResults.get(0).getUserNumber());
        }
        return "search"; // search.html로 포워딩
    }
}
