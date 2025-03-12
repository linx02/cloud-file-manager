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
    private final String tagIndexName = "TagLookupIndex";

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
        if (tags == null || tags.isEmpty()) {
            tags = List.of("untagged");
        }

        for (String tag : tags) {
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("file_name", AttributeValue.builder().s(fileName).build());
            item.put("project_name", AttributeValue.builder().s(projectName).build());
            item.put("s3_url", AttributeValue.builder().s(s3Url).build());
            item.put("tag", AttributeValue.builder().s(tag.trim()).build());
            item.put("uploaded_at", AttributeValue.builder().s(new Date().toString()).build());

            try {
                dynamoDB.putItem(PutItemRequest.builder()
                        .tableName(tableName)
                        .item(item)
                        .build());
                System.out.println("File metadata stored for tag: " + tag);

            } catch (DynamoDbException e) {
                System.err.println("Error storing file metadata: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Map<String, AttributeValue> searchFileByName(String fileName) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("file_name = :fileName")
                .expressionAttributeValues(Map.of(
                        ":fileName", AttributeValue.builder().s(fileName).build()
                ))
                .build();

        QueryResponse queryResponse = dynamoDB.query(queryRequest);
        if (!queryResponse.items().isEmpty()) {
            return queryResponse.items().get(0);
        }
        return Collections.emptyMap();
    }

    public List<Map<String, AttributeValue>> searchFilesByTags(String tag) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .indexName("TagLookupIndex")
                .keyConditionExpression("tag = :tagValue")
                .expressionAttributeValues(Map.of(
                        ":tagValue", AttributeValue.builder().s(tag).build()
                ))
                .build();

        QueryResponse queryResponse = dynamoDB.query(queryRequest);

        if (queryResponse.hasItems()) {
            return queryResponse.items();
        }
        return new ArrayList<>();
    }
}