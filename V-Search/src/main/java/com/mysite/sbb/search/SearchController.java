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
            System.out.println(searchResults);
        }
        return "search"; // search.html로 포워딩
    }
}