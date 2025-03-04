package org.linx.service;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import java.util.*;

public class DynamoDBService {

    private final DynamoDbClient dynamoDB;
    private final String tableName;

    public DynamoDBService() {
        Properties props = new Properties();
        try {
            props.load(new java.io.FileInputStream(".env"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load AWS credentials from .env file", e);
        }

        this.tableName = props.getProperty("DYNAMODB_TABLE_NAME");

        this.dynamoDB = DynamoDbClient.builder()
                .region(Region.of(props.getProperty("AWS_REGION")))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                props.getProperty("AWS_ACCESS_KEY"),
                                props.getProperty("AWS_SECRET_KEY"))
                ))
                .build();
    }

    public void saveFileMetadata(String fileName, String projectName, String s3Url, List<String> tags) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("project_name", AttributeValue.builder().s(projectName).build());
        item.put("file_name", AttributeValue.builder().s(fileName).build());
        item.put("s3_url", AttributeValue.builder().s(s3Url).build());
        item.put("uploaded_at", AttributeValue.builder().s(new Date().toString()).build());

        if (tags != null && !tags.isEmpty()) {
            Set<String> cleanedTags = new HashSet<>();
            for (String tag : tags) {
                cleanedTags.add(tag.trim());
            }
            item.put("tags", AttributeValue.builder().ss(cleanedTags).build());
        } else {
            item.put("tags", AttributeValue.builder().ss(Collections.singletonList("untagged")).build());
        }

        try {
            dynamoDB.putItem(PutItemRequest.builder()
                    .tableName(tableName)
                    .item(item)
                    .build());
            System.out.println("File metadata stored in DynamoDB: " + fileName);

        } catch (DynamoDbException e) {
            System.err.println("Error storing file metadata in DynamoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, AttributeValue> searchFileByName(String fileName) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .filterExpression("file_name = :fileName")
                .expressionAttributeValues(Map.of(
                        ":fileName", AttributeValue.builder().s(fileName).build()
                ))
                .build();

        ScanResponse scanResponse = dynamoDB.scan(scanRequest);
        if (!scanResponse.items().isEmpty()) {
            return scanResponse.items().get(0);
        }
        return Collections.emptyMap();
    }

    public List<Map<String, AttributeValue>> searchFilesByTags(List<String> tags) {
        String filterExpression = "attribute_exists(tags)";
        Map<String, AttributeValue> expressionValues = new HashMap<>();

        for (int i = 0; i < tags.size(); i++) {
            filterExpression += " AND contains(tags, :tag" + i + ")";
            expressionValues.put(":tag" + i, AttributeValue.builder().s(tags.get(i).trim()).build());
        }

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .filterExpression(filterExpression)
                .expressionAttributeValues(expressionValues)
                .build();

        ScanResponse scanResponse = dynamoDB.scan(scanRequest);

        if (scanResponse.hasItems()) {
            return scanResponse.items();
        }

        return new ArrayList<>();
    }
}