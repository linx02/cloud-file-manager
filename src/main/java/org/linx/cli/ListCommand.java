package org.linx.cli;

import org.linx.service.DatabaseService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "list", description = "List files in a project")
public class ListCommand implements Runnable {

    DatabaseService databaseService = new DatabaseService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @Override
    public void run() {
        if (!databaseService.projectExists(projectName)) {
            System.out.println("Project does not exist");
            return;
        }

        try {
            List<String> files = databaseService.listFiles(projectName);

            if (files.isEmpty()) {
                System.out.println("No files in project");
                return;
            }

            System.out.println("Files in project " + projectName + ":");
            for (String file : files) {
                System.out.println(file);
            }

        } catch (Exception e) {
            System.out.println("Error listing files: " + e.getMessage());
        }
    }
}
