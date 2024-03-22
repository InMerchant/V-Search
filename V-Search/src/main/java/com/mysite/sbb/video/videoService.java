package com.mysite.sbb.video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class videoService {
   @Autowired
   private videoRepository vR;
   
   public List<video> getAllVideos(){
      return vR.findAll();
   }
   private final String uploadDir = "\\\\192.168.34.15\\last_project"; // 실제 파일을 업로드할 디렉토리 경로를 지정합니다.

    public void uploadFile(MultipartFile file) throws IOException {
        // 파일명에 UUID를 추가하여 중복을 방지합니다.
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // 파일을 지정된 경로에 저장합니다.
        Files.write(filePath, file.getBytes());
    }
}