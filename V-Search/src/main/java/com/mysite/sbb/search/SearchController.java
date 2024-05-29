package com.mysite.sbb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.video.Video;
import com.mysite.sbb.video.VideoRepository;

@Controller
public class SearchController {

	private final SearchService searchService;
	private VideoRepository vr;


    @Autowired
    public SearchController(SearchService searchService, VideoRepository videoRepository) {
        this.searchService = searchService;
        this.vr = videoRepository;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            Page<Search> searchPage = searchService.searchPage(keyword, page);            
            if (!searchPage.isEmpty()) {
                for (Search search : searchPage.getContent()) {
                    int videoNumber = search.getVideoNumber();
                    Video video = vr.findByVideoNo(videoNumber);
                    String videoUrl = video.getSTOURL();
                    search.setVideoUrl(videoUrl);
                }
                model.addAttribute("searchPage", searchPage);
            } else {
                model.addAttribute("message", "검색 결과가 없습니다."); // 검색 결과가 없을 때 메시지 추가
            }
        }
        model.addAttribute("keyword", keyword); // 검색어를 모델에 추가
        return "search"; // search.html로 포워딩
    }
}