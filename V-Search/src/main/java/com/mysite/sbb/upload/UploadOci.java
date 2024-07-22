package com.mysite.sbb.upload;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadResponse;

@Component
public class UploadOci {

    public void uploadOracle(File file, String title) throws Exception {
        // 구성 파일 로드
        ClassPathResource resource = new ClassPathResource("key/config");
        File configurationFile = resource.getFile();
        String configurationFilePath = configurationFile.getAbsolutePath();

        // 구성 파일 디렉토리 경로 추출
        String configDir = configurationFile.getParent();

        // pem 파일 경로 생성
        File pemFile = new File(Paths.get(configDir, "oci_api_key.pem").toString());

        // pem 파일 존재 여부 확인
        if (!pemFile.exists()) {
            throw new Exception("PEM 파일을 찾을 수 없음: " + pemFile.getAbsolutePath());
        }
        // pem 파일 읽기 권한 확인
        if (!pemFile.canRead()) {
            throw new Exception("PEM 파일을 읽을 수 없음: " + pemFile.getAbsolutePath());
        }

        // 수정된 구성 파일을 임시 파일에 저장
        File tempConfigFile = File.createTempFile("oci_config", ".tmp");

        // 원래 구성 파일을 읽고 key_file 경로를 수정
        List<String> lines = Files.readAllLines(configurationFile.toPath());
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("key_file=")) {
                lines.set(i, "key_file=" + pemFile.getAbsolutePath());
                break;
            }
        }
        // 수정된 내용을 임시 파일에 작성
        Files.write(tempConfigFile.toPath(), lines);

        // 임시 구성 파일을 사용하여 인증 정보 제공자 생성
        String profile = "DEFAULT";
        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(tempConfigFile.getAbsolutePath(), profile);

        final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

        ObjectStorage client = ObjectStorageClient.builder().region(Region.AP_CHUNCHEON_1).build(provider);

        // 업로드 설정 구성
        UploadConfiguration uploadConfiguration = UploadConfiguration.builder()
                .allowMultipartUploads(true)
                .allowParallelUploads(true)
                .build();

        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

        String namespaceName = "axekzvuz7vol";
        String bucketName = "bucket-20240503-1000";
        String objectName = title + "/" + title + ".mp4";
        Map<String, String> metadata = null;
        String contentType = "video/mp4";
        String contentEncoding = null;
        String contentLanguage = null;
        File body = file;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucketName(bucketName)
                .namespaceName(namespaceName)
                .objectName(objectName)
                .contentType(contentType)
                .contentLanguage(contentLanguage)
                .contentEncoding(contentEncoding)
                .opcMeta(metadata)
                .build();

        UploadRequest uploadDetails = UploadRequest.builder(body)
                .allowOverwrite(true)
                .build(request);

        // 업로드 요청 및 결과 출력
        UploadResponse response = uploadManager.upload(uploadDetails);
        System.out.println(response);

        // 업로드된 객체 가져오기
        GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder()
                .namespaceName(namespaceName)
                .bucketName(bucketName)
                .objectName(objectName)
                .build());

        // 업로드된 객체의 메타데이터 출력
        System.out.println(getResponse.getOpcMeta());

        // 스트림 콘텐츠는 업로드된 파일과 일치해야 합니다.
        try (final InputStream fileStream = getResponse.getInputStream()) {
            // fileStream 사용
        } // try-with-resources가 fileStream을 자동으로 닫습니다.

        // 임시 파일 삭제
        tempConfigFile.delete();
    }
}
