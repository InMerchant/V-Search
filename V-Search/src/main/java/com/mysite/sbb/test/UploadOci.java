package com.mysite.sbb.test;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

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

public class UploadOci {

	public static void main(String[] args) throws Exception {
        String configurationFilePath = "~/key/config";
        String profile = "DEFAULT";

        String namespaceName = "axekzvuz7vol";
        String bucketName = "bucket-20240503-1000";
        String objectName = "test/video1.png"; // 클라우드 저장 경로
        File body = new File(System.getProperty("user.home") + File.separator + "video" + File.separator + "test.mp4"); // 업로드할 파일을 가리키는 File 객체
        String contentType = "video/mp4"; // 업로드할 파일의 컨텐츠 타입 (예: "image/png", "application/pdf" 등)
        Map<String, String> metadata = null; // 추가할 메타데이터가 없다면 null
        String contentEncoding = null; // 압축방식 예: "gzip", "deflate" 등, 없으면 null
        String contentLanguage = null; // 파일의 언어 (예: "en", "ko", "jp" 등), 언어 정보가 없다면 null

        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);

        final ConfigFileAuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configFile);

        ObjectStorage client =
                ObjectStorageClient.builder().region(Region.AP_CHUNCHEON_1).build(provider);

        // configure upload settings as desired
        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                        .allowMultipartUploads(true)
                        .allowParallelUploads(true)
                        .build();

        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucketName(bucketName)
                        .namespaceName(namespaceName)
                        .objectName(objectName)
                        .contentType(contentType)
                        .contentLanguage(contentLanguage)
                        .contentEncoding(contentEncoding)
                        .opcMeta(metadata)
                        .build();

        UploadRequest uploadDetails =
                UploadRequest.builder(body).allowOverwrite(true).build(request);

        // upload request and print result
        // if multi-part is used, and any part fails, the entire upload fails and will throw
        // BmcException
        UploadResponse response = uploadManager.upload(uploadDetails);
        System.out.println(response);

        // fetch the object just uploaded
        GetObjectResponse getResponse =
                client.getObject(
                        GetObjectRequest.builder()
                                .namespaceName(namespaceName)
                                .bucketName(bucketName)
                                .objectName(objectName)
                                .build());

        // use the response's function to print the fetched object's metadata
        System.out.println(getResponse.getOpcMeta());

        // stream contents should match the file uploaded
        try (final InputStream fileStream = getResponse.getInputStream()) {
            // use fileStream
        } // try-with-resources automatically closes fileStream
    }

}
