package com.mysite.sbb.upload;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
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
		String configurationFilePath = "~/key/config";
		String profile = "DEFAULT";

		String namespaceName = "axekzvuz7vol";
		String bucketName = "bucket-20240503-1000";
		String objectName = title+"/"+title+".mp4";
		Map<String, String> metadata = null;
		String contentType = "video/mp4";
		String contentEncoding = null;
		String contentLanguage = null;
		File body = file;

		final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);

		final ConfigFileAuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(
				configFile);

		ObjectStorage client = ObjectStorageClient.builder().region(Region.AP_CHUNCHEON_1).build(provider);

		// configure upload settings as desired
		UploadConfiguration uploadConfiguration = UploadConfiguration.builder().allowMultipartUploads(true)
				.allowParallelUploads(true).build();

		UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

		PutObjectRequest request = PutObjectRequest.builder().bucketName(bucketName).namespaceName(namespaceName)
				.objectName(objectName).contentType(contentType).contentLanguage(contentLanguage)
				.contentEncoding(contentEncoding).opcMeta(metadata).build();

		UploadRequest uploadDetails = UploadRequest.builder(body).allowOverwrite(true).build(request);

		// upload request and print result
		// if multi-part is used, and any part fails, the entire upload fails and will
		// throw
		// BmcException
		UploadResponse response = uploadManager.upload(uploadDetails);
		System.out.println(response);

		// fetch the object just uploaded
		GetObjectResponse getResponse = client.getObject(GetObjectRequest.builder().namespaceName(namespaceName)
				.bucketName(bucketName).objectName(objectName).build());

		// use the response's function to print the fetched object's metadata
		System.out.println(getResponse.getOpcMeta());

		// stream contents should match the file uploaded
		try (final InputStream fileStream = getResponse.getInputStream()) {
			// use fileStream
		} // try-with-resources automatically closes fileStream
	}

}