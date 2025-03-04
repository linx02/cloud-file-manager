package org.linx.cli;

import org.linx.service.DatabaseService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "upload", description = "Upload a file")
public class UploadCommand implements Runnable {

    DatabaseService databaseService = new DatabaseService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "File name")
    private String fileName;

    @CommandLine.Option(names = {"-d", "--destination"}, required = false, description = "Destination")
    private String destination;

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

            // IMPLEMENT ME
            System.out.println("Uploading file " + fileName);

        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }
}
