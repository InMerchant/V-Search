package com.mysite.sbb.search;

//SearchController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class SearchController {

 @Autowired
 private SearchService searchService;

 @GetMapping("/search")
 public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
     if(keyword != null && !keyword.isEmpty()) {
         List<Search> searchResults = searchService.search(keyword);
         model.addAttribute("searchResults", searchResults);
     }
     return "search"; // search.html로 포워딩
 }
}

