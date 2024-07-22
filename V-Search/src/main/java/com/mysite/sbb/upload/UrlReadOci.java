package com.mysite.sbb.upload;

import java.io.InputStream;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageAsync;
import com.oracle.bmc.objectstorage.ObjectStorageAsyncClient;
import com.oracle.bmc.objectstorage.model.BucketSummary;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.ListBucketsRequest;
import com.oracle.bmc.objectstorage.requests.ListBucketsRequest.Builder;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.ListBucketsResponse;
import com.oracle.bmc.responses.AsyncHandler;

@Component
public class UrlReadOci {

    public String VideoUrl(String title) throws Exception {

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

        String profile = "DEFAULT";
        String region = "ap-chuncheon-1";
        String bucketName = "bucket-20240503-1000";
        String objectName = title + "/" + title + ".mp4";

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
        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(tempConfigFile.getAbsolutePath(), profile);

        final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
        ObjectStorageAsync client = ObjectStorageAsyncClient.builder().region(Region.AP_CHUNCHEON_1).build(provider);

        // 네임스페이스 가져오기
        ResponseHandler<GetNamespaceRequest, GetNamespaceResponse> namespaceHandler = new ResponseHandler<>();
        client.getNamespace(GetNamespaceRequest.builder().build(), namespaceHandler);
        GetNamespaceResponse namespaceResponse = namespaceHandler.waitForCompletion();

        String namespaceName = namespaceResponse.getValue();
        System.out.println("사용 중인 네임스페이스: " + namespaceName);

        Builder listBucketsBuilder = ListBucketsRequest.builder().namespaceName(namespaceName).compartmentId(provider.getTenantId());

        String nextToken = null;
        do {
            listBucketsBuilder.page(nextToken);
            ResponseHandler<ListBucketsRequest, ListBucketsResponse> listBucketsHandler = new ResponseHandler<>();
            client.listBuckets(listBucketsBuilder.build(), listBucketsHandler);
            ListBucketsResponse listBucketsResponse = listBucketsHandler.waitForCompletion();
            for (BucketSummary bucket : listBucketsResponse.getItems()) {
                System.out.println("버킷 발견: " + bucket.getName());
            }
            nextToken = listBucketsResponse.getOpcNextPage();
        } while (nextToken != null);

        String objectURL = null;

        // 오브젝트 스토리지에서 업로드된 파일 가져오기
        ResponseHandler<GetObjectRequest, GetObjectResponse> objectHandler = new ResponseHandler<>();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .namespaceName(namespaceName)
                .bucketName(bucketName)
                .objectName(objectName)
                .build();
        client.getObject(getObjectRequest, objectHandler);
        GetObjectResponse getResponse = objectHandler.waitForCompletion();

        // 스트림 콘텐츠는 업로드된 파일과 일치해야 합니다.
        try (final InputStream fileStream = getResponse.getInputStream()) {
            // fileStream 사용
            objectURL = String.format("https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                    region, namespaceName, bucketName, objectName);
            System.out.println("오브젝트 URL: " + objectURL);
        } // try-with-resources가 fileStream을 자동으로 닫습니다.

        // 임시 파일 삭제
        tempConfigFile.delete();

        return objectURL;
    }

    private static class ResponseHandler<IN, OUT> implements AsyncHandler<IN, OUT> {
        private OUT item;
        private Throwable failed = null;
        private CountDownLatch latch = new CountDownLatch(1);

        private OUT waitForCompletion() throws Exception {
            latch.await();
            if (failed != null) {
                if (failed instanceof Exception) {
                    throw (Exception) failed;
                }
                throw (Error) failed;
            }
            return item;
        }

        @Override
        public void onSuccess(IN request, OUT response) {
            item = response;
            latch.countDown();
        }

        @Override
        public void onError(IN request, Throwable error) {
            failed = error;
            latch.countDown();
        }
    }
}
