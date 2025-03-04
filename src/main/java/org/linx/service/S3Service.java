package org.linx.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

public class S3Service {

    private final S3Client s3;
    private final String bucketName;

    public S3Service() {
        Properties props = new Properties();
        try {
            props.load(new java.io.FileInputStream(".env"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load .env file: ", e);
        }

        String accessKey = props.getProperty("AWS_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_SECRET_KEY");
        String region = props.getProperty("AWS_REGION");
        this.bucketName = props.getProperty("AWS_BUCKET_NAME");

        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    public String uploadFile(String filePath, String projectName) {
        String s3Key = projectName + "/" + Paths.get(filePath).getFileName();
        File file = new File(filePath);

        try {
            s3.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .build(),
                    RequestBody.fromFile(file));

            String s3Url = "https://" + bucketName + ".s3.amazonaws.com/" + s3Key;
            System.out.println("Uploaded: " + filePath + " → " + s3Url);
            return s3Url;

        } catch (S3Exception e) {
            System.err.println("Error uploading file: " + e.getMessage());
            return null;
        }
    }

    public void downloadFile(String fileName, String projectName, String destinationPath) {
        String s3Key = projectName + "/" + fileName;

        try {
            s3.getObject(GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .build(),
                    Paths.get(destinationPath, fileName));

            System.out.println("Downloaded: " + fileName + " → " + destinationPath);

        } catch (S3Exception e) {
            System.err.println("Error downloading file: " + e.getMessage());
        }
    }
}