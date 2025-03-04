package org.linx.cli;

import org.linx.service.DatabaseService;
import org.linx.service.DynamoDBService;
import org.linx.service.S3Service;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommandLine.Command(name = "upload", description = "Upload a file")
public class UploadCommand implements Runnable {

    DatabaseService databaseService = new DatabaseService();
    DynamoDBService dynamoDBService = new DynamoDBService();
    S3Service s3Service = new S3Service();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "File name")
    private String fileName;

    @CommandLine.Option(names = {"-t", "--tags"}, required = true, split = ",", description = "Comma-separated tags")
    private String[] tags;

    @Override
    public void run() {
        if (!databaseService.projectExists(projectName)) {
            System.out.println("Project does not exist");
            return;
        }

        try {
            List<String> files = databaseService.listFiles(projectName);

            if (files.contains(fileName)) {
                System.out.println("File already exists in project");
                return;
            }

            String fileUrl = s3Service.uploadFile(fileName, projectName);
            databaseService.addFile(projectName, fileName, fileUrl);

            List<String> tagList = tags != null ? Arrays.asList(tags) : new ArrayList<>();
            dynamoDBService.saveFileMetadata(fileName, projectName, fileUrl, tagList);


        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }
}
