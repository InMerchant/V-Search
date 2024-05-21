package com.mysite.sbb.video;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.springframework.security.core.userdetails.UserDetails;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mysite.sbb.User.UserService;
import com.mysite.sbb.upload.UploadOci;
import com.mysite.sbb.upload.UrlReadOci;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class VideoService {
	@Autowired
	private UserService userService;
	@Autowired
	private VideoRepository vR;
	@Autowired
	private JdbcTemplate jT;
	@Autowired
	private UploadOci UO;
	@Autowired
	private UrlReadOci UR;

	public List<Video> getAllVideo() {
		return vR.findAll();
	}

	public List<Object[]> getAllVideoNamesAndUserNumbers() {
		return vR.findAllVideoNamesAndUserNumbers();
	}

	@Transactional
	public void uploadFile(File file, String title, int summary) throws IOException {
		// 파일명에 UUID를 추가하여 중복을 방지합니다.
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		int userNo = userService.getUserNO(username);
		String URL = null;
		try {
			UO.uploadOracle(file, title);
			URL = UR.VideoUrl(title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Video Video = new Video();
		Video.setUSER_NO(userNo);
		Video.setVIDEO_NAME(title);
		Video.setSummary_chk(summary);
		Video.setSTOURL(URL);
		// 비디오 엔티티 저장
		vR.save(Video);
	}

	public String videoPlay(int videoNO) throws SQLException {
		String sql = "SELECT STOURL FROM bae.VIDEO WHERE VIDEO_NO=?";
		String url = jT.queryForObject(sql, new Object[] { videoNO }, String.class);
		return url;
	}

	public Video getVideoByNo(int videoNo) {
		return vR.findByVideoNo(videoNo);
	}

}
