package org.linx.cli;

import org.linx.service.DatabaseService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "download", description = "Download a file")
public class DownloadCommand implements Runnable {

    DatabaseService databaseService = new DatabaseService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "File name")
    private String fileName;

    @Override
    public void run() {
        if (!databaseService.projectExists(projectName)) {
            System.out.println("Project does not exist");
            return;
        }

        try {
            List<String> files = databaseService.listFiles(projectName);

            if (!files.contains(fileName)) {
                System.out.println("File does not exist in project");
                return;
            }

            // IMPLEMENT ME
            System.out.println("Downloading file " + fileName);

        } catch (Exception e) {
            System.out.println("Error downloading file: " + e.getMessage());
        }
    }
}
