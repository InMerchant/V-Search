package com.mysite.sbb.test;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

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

public class OciUtilController {

    public static void main(String[] args) throws Exception {

        String configurationFilePath = "~/key/config";
        String profile = "DEFAULT";
        String region = "ap-chuncheon-1";
        String bucketName = "bucket-20240503-1000";
        String objectName = "test.mp4";

        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);

        final AuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configFile);
        ObjectStorageAsync client =
                ObjectStorageAsyncClient.builder().region(Region.AP_CHUNCHEON_1).build(provider);

        ResponseHandler<GetNamespaceRequest, GetNamespaceResponse> namespaceHandler =
                new ResponseHandler<>();
        client.getNamespace(GetNamespaceRequest.builder().build(), namespaceHandler);
        GetNamespaceResponse namespaceResponse = namespaceHandler.waitForCompletion();

        String namespaceName = namespaceResponse.getValue();
        System.out.println("Using namespace: " + namespaceName);

        Builder listBucketsBuilder =
                ListBucketsRequest.builder()
                        .namespaceName(namespaceName)
                        .compartmentId(provider.getTenantId());

        String nextToken = null;
        do {
            listBucketsBuilder.page(nextToken);
            ResponseHandler<ListBucketsRequest, ListBucketsResponse> listBucketsHandler =
                    new ResponseHandler<>();
            client.listBuckets(listBucketsBuilder.build(), listBucketsHandler);
            ListBucketsResponse listBucketsResponse = listBucketsHandler.waitForCompletion();
            for (BucketSummary bucket : listBucketsResponse.getItems()) {
                System.out.println("Found bucket: " + bucket.getName());
            }
            nextToken = listBucketsResponse.getOpcNextPage();
        } while (nextToken != null);

        // fetch the uploaded file from object storage
        ResponseHandler<GetObjectRequest, GetObjectResponse> objectHandler =
                new ResponseHandler<>();
        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .namespaceName(namespaceName)
                        .bucketName(bucketName)
                        .objectName(objectName)
                        .build();
        client.getObject(getObjectRequest, objectHandler);
        GetObjectResponse getResponse = objectHandler.waitForCompletion();

        // stream contents should match the file uploaded
        try (final InputStream fileStream = getResponse.getInputStream()) {
            // use fileStream
        	String objectURL = String.format("https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
        	                                region, namespaceName, bucketName, objectName);
        	System.out.println("Object URL: " + objectURL);
        } // try-with-resources automatically closes fileStream

        client.close();
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