package com.mysite.sbb.video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.sbb.ResponseDto;
import com.mysite.sbb.User.UserService;
import com.mysite.sbb.recommend.RecommendService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class videoController {
	@Autowired
	private videoService videoService;
	@Autowired
	private UserService userService;
	@Autowired
	private RecommendService recommendService;
	
	@GetMapping(value="/video/{no}")
	public String videoDetail(@PathVariable("no") int videoNo, Model model) throws SQLException {
	    byte[] videoData = videoService.videoPlay(videoNo);
	    String base64EncodedVideoData = Base64.getEncoder().encodeToString(videoData);
	    model.addAttribute("videoData", base64EncodedVideoData);
	    
	    
	    // 비디오 추천 상태 및 추천 수를 가져옴
	    videoService.recommendTest(videoNo); // 추천 상태 및 추천 수를 설정
	    
	    // 해당 비디오에 대한 추천 상태와 추천 수를 모델에 추가
	    video video = videoService.getVideoByNo(videoNo);
	    model.addAttribute("recommendState", video.isRecommend_state());
	    model.addAttribute("recommendCount", video.getRecommend_count());
	    
	    
	    return "videoDetail";
	}
	
	@GetMapping("/")
	public String mainPage(Model model) {
		List<Object[]> videoDetails = videoService.getAllVideoNamesAndUserNumbers();
		model.addAttribute("videoDetails", videoDetails);
		return "mainpage";
	}
	@GetMapping("/uploadForm")
    public String showUploadForm() {
        return "upload"; 
    }
    @PostMapping("/upload")
    @Secured("ROLE_USER") 
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,Model model) {
    	System.out.println("File object: " + file);
    	System.out.println("File size: " + file.getSize());
    	if ((file == null ||  file.isEmpty()) || (title == null || title.isEmpty())) {
    	    model.addAttribute("message", "Please select a file and enter a title to upload.");
    	    return "uploadResult";
    	}
        try {
            videoService.uploadFile(file,title); 
            model.addAttribute("message", "File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            model.addAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
            e.printStackTrace();
        }
        return "uploadResult";
    }
    @GetMapping("/video/test")
    public String test() {
        return "test";
    }
    
    @PostMapping("/video/{no}/recommend")
    public ResponseDto<Integer> recommend(@PathVariable("no") long video_no) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	int userNo = userService.getUserNO(username);
    	recommendService.recommend(video_no, userNo);
        return new ResponseDto<Integer>(HttpStatus.CREATED.value(), 1);
    }

    @DeleteMapping("/video/{no}/recommend")
    public ResponseDto<Integer> cancelRecommend(@PathVariable("no") Long video_no) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	int userNo = userService.getUserNO(username);
        recommendService.cancelRecommend(video_no, userNo);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    
}