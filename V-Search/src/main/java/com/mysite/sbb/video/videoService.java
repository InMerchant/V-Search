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
   
   @Transactional
   public void uploadFile(MultipartFile file, String title) throws IOException {
       // 파일명에 UUID를 추가하여 중복을 방지합니다.
       String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
       String username = SecurityContextHolder.getContext().getAuthentication().getName();
       int userNo = userService.getUserNO(username);
       byte[] fileBytes = file.getBytes();
       video video = new video();
       video.setUSER_NO(userNo); 
       video.setVIDEO_NAME(title);
       video.setSTORAGE(fileBytes); 

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
	
	
	public video getVideoByNo(int videoNo) {
        return vR.findByVideoNo(videoNo);
    }
	
	


}