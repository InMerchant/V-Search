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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysite.sbb.ResponseDto;
import com.mysite.sbb.User.UserProfileDto;
import com.mysite.sbb.User.UserService;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.mysite.sbb.delvideo.Del;
import com.mysite.sbb.delvideo.DelService;
import com.mysite.sbb.recommend.Recommend;
import com.mysite.sbb.recommend.RecommendRepository;
import com.mysite.sbb.recommend.RecommendService;

@RequiredArgsConstructor
@Controller
@EnableTransactionManagement
public class VideoController {
	@Autowired
	private VideoService videoService;
	@Autowired
	private UserService userService;
	@Autowired
	private DelService delService;
	private final RecommendService recommendService;
	@Autowired
	private EntityManager entityManager;

	@GetMapping(value = "/video/{no}")
	public String videoDetail(@PathVariable("no") int videoNo, Model model) throws SQLException {
		byte[] videoData = videoService.videoPlay(videoNo);
		// 비디오에 대한 유저 구독 정보 삽입
		Del video = entityManager.find(Del.class, videoNo);
		int videoUserId = video.getUsernumber();
		UserProfileDto userDto = userService.userProfile(videoUserId, videoNo);
		model.addAttribute("userDto", userDto);

		String base64EncodedVideoData = Base64.getEncoder().encodeToString(videoData);
		model.addAttribute("videoData", base64EncodedVideoData);
		model.addAttribute("videoNo", videoNo);
		int recommendationsCount = recommendService.getRecommendationsCountByVideoNo(videoNo);
		model.addAttribute("recommendationsCount", recommendationsCount);
		return "videoDetail";
	}

	@PostMapping("/delvideos/{videoNo}")
	public String deleteVideo(@PathVariable("videoNo") String videoNoString, RedirectAttributes redirectAttributes) {
		try {
			int videoNo = Integer.parseInt(videoNoString);
			boolean deletionResult = delService.deleteVideo(videoNo);
			if (deletionResult) {
				redirectAttributes.addFlashAttribute("message", "동영상이 성공적으로 삭제되었습니다.");
			} else {
				redirectAttributes.addFlashAttribute("message", "동영상 삭제에 실패하였습니다. 다시 시도해주세요.");
			}
			return "redirect:/"; // 사용자의 동영상 목록 페이지로 리다이렉션
		} catch (NumberFormatException e) {
			// 경로 변수를 정수로 변환할 수 없는 경우 예외 처리
			throw new IllegalArgumentException("Invalid video number: " + videoNoString);
		}
	}

	@RestController
	public class RecommendController {

		private final RecommendService recommendService;

		@Autowired
		public RecommendController(RecommendService recommendService) {
			this.recommendService = recommendService;
		}

		@PostMapping("/recommend/toggle/{videoNo}")
		public ResponseEntity<String> toggleRecommendation(@PathVariable("videoNo") int videoNo) {
			try {
				recommendService.toggleRecommendation(videoNo);
				return ResponseEntity.ok("Recommendation toggled successfully");
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error toggling recommendation: " + e.getMessage());
			}
		}
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
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
			@RequestParam("summary") int summary, Model model) {
		System.out.println("File object: " + file);
		System.out.println("File size: " + file.getSize());
		if ((file == null || file.isEmpty()) || (title == null || title.isEmpty())) {
			model.addAttribute("message", "Please select a file and enter a title to upload.");
			return "uploadResult";
		}
		try {
			videoService.uploadFile(file, title, summary);
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