package org.linx.cli;

import org.linx.service.DatabaseService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "delete", description = "Delete a file")
public class DeleteCommand implements Runnable {

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

            String confirm = System.console().readLine("Are you sure you want to delete the file? (y/n): ");
            if (!confirm.equals("y")) {
                System.out.println("Aborted");
                return;
            }

            // IMPLEMENT ME
            System.out.println("Deleting file " + fileName);

        } catch (Exception e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
    }
}
