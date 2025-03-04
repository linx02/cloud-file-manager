package org.linx.cli;

import org.linx.service.DatabaseService;
import org.linx.service.DynamoDBService;
import picocli.CommandLine;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

@CommandLine.Command(name = "search", description = "Search for a file")
public class SearchCommand implements Runnable {

    private final DatabaseService databaseService = new DatabaseService();
    private final DynamoDBService dynamoDBService = new DynamoDBService();

    @CommandLine.Option(names = {"-p", "--project"}, required = false, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-f", "--file"}, required = false, description = "File name")
    private String fileName;

    @CommandLine.Option(names = {"-t", "--tags"}, required = false, split = ",", description = "Comma-separated tags")
    private String[] tags;

    @Override
    public void run() {
        try {
            List<String> matchingFiles = new ArrayList<>();

            if (fileName != null && projectName != null) {
                Map<String, AttributeValue> fileMetadata = dynamoDBService.searchFileByName(fileName);
                if (!fileMetadata.isEmpty()) {
                    System.out.println("\nFound file in DynamoDB:");
                    printFileMetadata(fileMetadata);
                    return;
                }
            }

            if (tags != null) {
                List<Map<String, AttributeValue>> tagResults = dynamoDBService.searchFilesByTags(Arrays.asList(tags));
                if (!tagResults.isEmpty()) {
                    System.out.println("\nFound files by tags in DynamoDB:");
                    tagResults.forEach(this::printFileMetadata);
                    return;
                }
            }

            if (projectName != null) {
                if (!databaseService.projectExists(projectName)) {
                    System.out.println("Project not found: " + projectName);
                    return;
                }
                matchingFiles = databaseService.listFiles(projectName);
            } else {
                List<String> projects = databaseService.getProjects();
                for (String project : projects) {
                    matchingFiles.addAll(databaseService.listFiles(project));
                }
            }

            if (!matchingFiles.isEmpty()) {
                System.out.println("\nFound file(s) in MySQL:");
                matchingFiles.forEach(System.out::println);
            } else {
                System.out.println("\nNo matching files found.");
            }

        } catch (Exception e) {
            System.err.println("Error searching for file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printFileMetadata(Map<String, AttributeValue> fileMetadata) {
        System.out.println("File: " + fileMetadata.get("file_name").s());
        System.out.println("Project: " + fileMetadata.get("project_name").s());
        System.out.println("S3 URL: " + fileMetadata.get("s3_url").s());
        System.out.println("Tags: " + fileMetadata.get("tags").ss());
        System.out.println("Uploaded At: " + fileMetadata.get("uploaded_at").s());
        System.out.println("--------------------------------------------------");
    }
}