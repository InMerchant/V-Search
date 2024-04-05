package com.mysite.sbb.video;

import java.io.ByteArrayOutputStream;
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
import com.mysite.sbb.recommend.Recommend;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class videoService {
	@Autowired
	private UserService userService;
   @Autowired
   private videoRepository vR;
   @Autowired
   private JdbcTemplate jT;
   
  public List<video> getAllVideo(){
	  return vR.findAll();
  }
  public List<Object[]> getAllVideoNamesAndUserNumbers() {
	    return vR.findAllVideoNamesAndUserNumbers();
	}
   
   private final String uploadDir = "\\\\192.168.34.15\\last_project"; // 실제 파일을 업로드할 디렉토리 경로를 지정합니다.
   @Transactional
   public void uploadFile(MultipartFile file, String title) throws IOException {
       // 파일명에 UUID를 추가하여 중복을 방지합니다.
       String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
       Path filePath = Paths.get(uploadDir, fileName);
       // 파일을 지정된 경로에 저장합니다.
       Files.write(filePath, file.getBytes());
       String username = SecurityContextHolder.getContext().getAuthentication().getName();
       int userNo = userService.getUserNO(username);
      
       video video = new video();
       video.setUSER_NO(userNo); 
       video.setVIDEO_NAME(title);
       video.setSTORAGE(Files.readAllBytes(filePath)); 

       // 비디오 엔티티 저장
       vR.save(video);
   }

	private byte[] blobToBytes(Blob blob) throws SQLException, IOException {
	    try (InputStream inputStream = blob.getBinaryStream()) {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[8192];
	        int bytesRead = -1;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        return outputStream.toByteArray();
	    }
	}
	public byte[] videoPlay(int videoNO) throws SQLException {
	    String sql = "SELECT STORAGE FROM bae.VIDEO WHERE VIDEO_NO=?";
	    List<byte[]> blobBytesList = jT.query(sql, new Object[]{videoNO}, (rs, rowNum) -> {
	        Blob blob = rs.getBlob("STORAGE");
	        try {
	            return blobToBytes(blob);
	        } catch (IOException e) {
	            return null;
	        }
	    });
	    if (blobBytesList.isEmpty()) {
	        throw new SQLException("No video found with the given name: " + videoNO);
	    }
	    byte[] blobBytes = blobBytesList.get(0);
	    if (blobBytes != null) {
	        return blobBytes;
	    } else {
	        throw new SQLException("BLOB data is null");
	    }

	}
	
	@Transactional
	public void recommendTest() {
	    video video = new video();
	    
	    // 수정: video.getRecommends()가 null이 아닌지 확인
	    List<Recommend> recommends = video.getRecommends();
	    if (recommends != null) {
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        int userNo = userService.getUserNO(username);

	        // 수정: 목록이 null이 아닌 경우에만 순회하도록 수정
	        recommends.forEach((recommend) -> {
	            if(recommend.getUser().getUSER_NO() == userNo) {
	                video.setRecommend_state(true);
	            }
	        });
	        // 수정: 목록이 null이 아닌 경우에만 사이즈를 가져와 설정
	        video.setRecommend_count(recommends.size());
	    }
	}


}