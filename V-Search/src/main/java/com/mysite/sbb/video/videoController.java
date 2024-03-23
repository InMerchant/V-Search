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
		List<video> Allvideo=videoService.getAllVideo();
		model.addAttribute("Allvideo",Allvideo);
		return "mainpage";
	}
	@GetMapping("/uploadForm")
    public String showUploadForm() {
        return "upload"; 
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
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

    @GetMapping("/downloadForm")
	public String downloadForm() {
		return "download";
	}
    @PostMapping("/download")
    public void downloadFile(@RequestParam("videoName") String videoName, HttpServletResponse response) throws IOException, SQLException {
        // 클라이언트로부터 전달된 비디오 이름을 사용하여 파일을 다운로드합니다.
        String filePath = "바탕 화면" + videoName; // 실제 파일 경로를 여기에 입력하세요.

        try {
            videoService.downloadFile(videoName, filePath);

            // 파일 다운로드를 위한 설정
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + videoName + "\"");

            // 파일을 읽어서 클라이언트로 전송
            response.getOutputStream().write(Files.readAllBytes(Paths.get(filePath)));
            response.getOutputStream().flush();
        } catch (SQLException | IOException e) {
            e.printStackTrace(); 
        }
    }
}