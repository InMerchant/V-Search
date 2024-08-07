package com.mysite.sbb.video;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.SM;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysite.sbb.ResponseDto;
import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.User.UserProfileDto;
import com.mysite.sbb.User.UserService;
import com.mysite.sbb.agechk.VideoChkService;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.comment.CommentService;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.mysite.sbb.delvideo.Del;
import com.mysite.sbb.delvideo.DelService;
import com.mysite.sbb.recommend.Recommend;
import com.mysite.sbb.recommend.RecommendRepository;
import com.mysite.sbb.recommend.RecommendService;
import com.mysite.sbb.timeline.TimeLine;
import com.mysite.sbb.timeline.TimeLineService;

@RequiredArgsConstructor
@Controller
@EnableTransactionManagement
public class VideoController {
	private final CommentService commentService;
	@Autowired
	private VideoService videoService;
	@Autowired
	private UserService userService;
	@Autowired
	private DelService delService;
	private final RecommendService recommendService;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private TimeLineService TS;
	@Autowired
	private VideoChkService VC;

	@GetMapping(value = "/video/{no}") // 최적화
	public String videoDetail(@PathVariable("no") int videoNo, Model model) throws SQLException {
		Del video = entityManager.find(Del.class, videoNo);
		int videoUserId = video.getUsernumber();
		UserProfileDto userDto = userService.userProfile(videoUserId, videoNo);
		model.addAttribute("userDto", userDto);
		model.addAttribute("videoNo", videoNo);
		List<TimeLine> SMYresult = TS.timeLineCaption(videoNo);
		model.addAttribute("SMYresult", SMYresult);
		Video scriptObj = videoService.findSmyScriptAndOBJ(videoNo);
		model.addAttribute("scriptObj", scriptObj);
		int recommendationsCount = recommendService.getRecommendationsCountByVideoNo(videoNo);
		model.addAttribute("recommendationsCount", recommendationsCount);
		List<Comment> comments = commentService.getCommentsByVideoNumber(videoNo);
		model.addAttribute("comments", comments);
		int age = VC.calculateVideoAge(videoNo);
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

	@PostMapping("/comments/{videoNo}/{id}")
	public String deleteComment(@PathVariable("id") int id) {
		commentService.deleteComment(id);
		// 삭제 후 리다이렉션할 URL을 반환합니다. 여기서는 원래 페이지로 리다이렉션합니다.
		return "redirect:/video/{videoNo}";
	}

	@RestController
	@RequestMapping("/recommend")
	public class RecommendController {
	   
	    private RecommendService recommendService;
		public RecommendController(RecommendService recommendService) {
	        this.recommendService = recommendService;
	    }

	    @PostMapping("/toggle/{videoNo}")
	    public ResponseEntity<String> toggleRecommendation(@PathVariable("videoNo") int videoNo) {
	        boolean isRecommended = recommendService.toggleRecommendation(videoNo);
	        if (isRecommended) {
	            return ResponseEntity.ok("추천되었습니다.");
	        } else {
	            return ResponseEntity.ok("추천이 취소되었습니다.");
	        }
	    }
	}


	@Controller
	public class CommentController {
		private final CommentService commentService;

		@Autowired
		public CommentController(CommentService commentService) {
			this.commentService = commentService;
		}

		@PostMapping("/comments/create/{videoNo}")
		public String createComment(HttpServletRequest request, @PathVariable("videoNo") Video videoNumber) {
			commentService.createComment(request, videoNumber);
			return "redirect:/video/{videoNo}";
		}
	}

	@GetMapping("/")
	public String mainPage(Model model) {
		List<Object[]> videoDetails = videoService.getAllVideoDetails();
		model.addAttribute("videoDetails", videoDetails);

		List<Integer> userNumbers = new ArrayList<>();
		List<Integer> videoAges = new ArrayList<>();

		for (Object[] videoDetail : videoDetails) {
			Integer userNumber = (Integer) videoDetail[2];
			userNumbers.add(userNumber);

			Integer videoNo = (Integer) videoDetail[0];
			int age = VC.calculateVideoAge(videoNo);
			videoAges.add(age);
			// System.out.println(age);
		}

		model.addAttribute("userNumbers", userNumbers);
		model.addAttribute("videoAges", videoAges);

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
		File tempFile = null;
		try {
			tempFile = convertMultipartFileToFile(file);
			videoService.uploadFile(tempFile, title, summary);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
		} finally {
			// 임시 파일 삭제
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
		return "uploadResult";
	}

	@GetMapping("/video/test/{no}")
	public String test(@PathVariable("no") int videoNo, Model model) throws SQLException {
		Del video = entityManager.find(Del.class, videoNo);
		int videoUserId = video.getUsernumber();
		UserProfileDto userDto = userService.userProfile(videoUserId, videoNo);
		model.addAttribute("userDto", userDto);
		model.addAttribute("videoNo", videoNo);
		List<TimeLine> SMYresult = TS.timeLineCaption(videoNo);
		model.addAttribute("SMYresult", SMYresult);
		Video scriptObj = videoService.findSmyScriptAndOBJ(videoNo);
		model.addAttribute("scriptObj", scriptObj);
		int recommendationsCount = recommendService.getRecommendationsCountByVideoNo(videoNo);
		model.addAttribute("recommendationsCount", recommendationsCount);
		List<Comment> comments = commentService.getCommentsByVideoNumber(videoNo);
		model.addAttribute("comments", comments);
		int age = VC.calculateVideoAge(videoNo);
		return "test";
	}

	private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		File tempFile = File.createTempFile("temp", null);
		multipartFile.transferTo(tempFile);
		return tempFile;
	}

}