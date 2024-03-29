package com.mysite.sbb.video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class videoController {
	@Autowired
	private videoService videoService;
	
	@GetMapping(value="/video/{no}")
	public ResponseEntity<Resource> videoDetail(@PathVariable("no") int videoNo) throws SQLException {
	    byte[] videoData = videoService.videoPlay(videoNo);
	    ByteArrayResource resource = new ByteArrayResource(videoData);

	    return ResponseEntity.ok()
	            .contentLength(videoData.length)
	            .contentType(MediaType.parseMediaType("video/mp4")) // 비디오 포맷에 맞게 변경하세요
	            .body(resource);
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
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
    	System.out.println(file);
    	if (file == null || file.isEmpty() || file.getSize() == 0) {
    	    model.addAttribute("message", "Please select a file to upload.");
    	    return "uploadResult";
    	}
        try {
            videoService.uploadFile(file); 
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
    
}