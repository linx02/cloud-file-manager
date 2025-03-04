package org.linx.cli;

import org.linx.service.DatabaseService;
import picocli.CommandLine;

@CommandLine.Command(name = "create-project", description = "Create a new project")
public class CreateProjectCommand implements Runnable {

    DatabaseService databaseService = new DatabaseService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @Override
    public void run() {
        if (databaseService.projectExists(projectName)) {
            System.out.println("Project already exists");
            return;
        }

        try {
            databaseService.createProject(projectName);
            System.out.println("Project created: " + projectName);
        } catch (Exception e) {
            System.out.println("Error creating project: " + e.getMessage());
        }
    }
}
