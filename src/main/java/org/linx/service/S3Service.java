package org.linx.service;

import java.util.HashMap;
import java.util.Map;

public class S3Service {
    private static final Map<String, String> mockS3Storage = new HashMap<>();

    public String uploadFile(String filePath, String projectName) {
        String s3Url = "s3://mock-bucket/" + projectName + "/" + filePath;
        mockS3Storage.put(filePath, s3Url);
        System.out.println("Mock: Uploaded file '" + filePath + "' to S3 (URL: " + s3Url + ")");
        return s3Url;
    }

    public void downloadFile(String fileName, String projectName) {
        String s3Url = mockS3Storage.get(fileName);
        if (s3Url != null) {
            System.out.println("Mock: Downloaded file '" + fileName + "' from " + s3Url);
        } else {
            System.out.println("Mock: File '" + fileName + "' not found in S3 storage.");
        }
    }

    public void deleteFile(String fileName, String projectName) {
        mockS3Storage.remove(fileName);
        System.out.println("Mock: Deleted file '" + fileName + "' from S3 storage.");
    }
}