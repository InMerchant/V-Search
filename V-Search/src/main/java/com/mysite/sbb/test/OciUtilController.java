package com.mysite.sbb.test;

import java.util.ArrayList;
import java.util.List;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetBucketRequest;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.GetBucketResponse;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oci-util")
public class OciUtilController {

    @GetMapping("/test")
    public void testOciUtil() {
        // OCI 설정 파일의 경로
        String configFile = "~/key/config";

        // OCI 설정 파일을 이용하여 인증 제공자 생성
        AuthenticationDetailsProvider provider = null;
        try {
            provider = new ConfigFileAuthenticationDetailsProvider(configFile, "DEFAULT");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 객체 저장소 클라이언트 초기화
        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.AP_CHUNCHEON_1);

        // 네임스페이스 가져오기
        GetNamespaceResponse namespaceResponse = client.getNamespace(GetNamespaceRequest.builder().build());
        String namespaceName = namespaceResponse.getValue();

        // 버킷 정보 가져오기
        String bucketName = "bucket-20240503-1000";
        GetBucketRequest request = GetBucketRequest.builder()
                .namespaceName(namespaceName)
                .bucketName(bucketName)
                .build();
        GetBucketResponse response = client.getBucket(request);

        // 가져온 버킷 정보 출력
        System.out.println("Bucket Name : " + response.getBucket().getName());
        System.out.println("Bucket Compartment : " + response.getBucket().getCompartmentId());
        System.out.println("The Approximate total number of objects within this bucket : "
                + response.getBucket().getApproximateCount());
        System.out.println("The Approximate total size of objects within this bucket : "
                + response.getBucket().getApproximateSize());
    }
}