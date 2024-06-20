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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.sbb.Call.CallService;
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
	@Autowired
	private CallService callService;

	public List<Video> getAllVideo() {
		return vR.findAll();
	}

	public List<Object[]> getAllVideoNamesAndUserNumbersAndSTOURL() {
		return vR.findAllVideoNamesAndUserNumbersAndSTOURL();
	}

	public List<Object[]> getAllVideoDetails() {
		// video_no, video_name, user_number, sto_url 등을 가져오는 쿼리
		return vR.findAllVideoDetails();
	}

	@ResponseBody
	public void uploadFile(File file, String title, int summary) throws IOException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		int userNo = userService.getUserNO(username);
		String URL = null;
		String SMYURL = null;
		String Url = "https://2d79-61-34-253-238.ngrok-free.app";
		try {
			UO.uploadOracle(file, title);
			URL = UR.VideoUrl(title);
			if (summary == 1) {
				callService.sendPostRequest(Url + "/aiload", title);
				Thread.sleep(1000);
				callService.sendPostRequest(Url + "/generate", title);
				Thread.sleep(1000);
				callService.sendPostRequest(Url + "/savevideo", title);
				Thread.sleep(1000);
				callService.sendPostRequest(Url + "/translate", title);
				SMYURL = UR.VideoUrl(title + "_smr");
			}
			Video Video = new Video();
			Video.setSMYURL(SMYURL);
			Video.setSTOURL(URL);
			Video.setSummary_chk(summary);
			Video.setUSER_NO(userNo);
			Video.setVIDEO_NAME(title);
			vR.save(Video);
			if (summary == 1) {
				Csvupload(userNo, title, Url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Csvupload(int userNo, String title, String URL) {
		Video videosave = vR.findByUserNoAndTitle(userNo, title);
		int videoNo = videosave.getVideoNo();
		callService.sendPostvideoNotitle(URL + "/saveinfo", videoNo, title);
	}

	public Video getVideoByNo(int videoNo) {
		return vR.findByVideoNo(videoNo);
	}

	public Video findSmyScriptAndOBJ(int videoNo) {
		return vR.findByVideoNo(videoNo);
	}

}
