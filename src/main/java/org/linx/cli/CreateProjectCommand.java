package org.linx.cli;

import org.linx.service.DatabaseService;
import picocli.CommandLine;

import java.util.List;

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

        System.out.println("Creating project " + projectName);
    }
}
