package org.linx.cli;

import org.linx.service.ProjectService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "create-project", description = "Create a new project")
public class CreateProjectCommand implements Runnable {

    ProjectService projectService = new ProjectService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @Override
    public void run() {
        List<String> projects = projectService.getProjects();
        if (projects.contains(projectName)) {
            System.out.println("Project " + projectName + " already exists");
            return;
        }

        System.out.println("Creating project " + projectName);
    }
}
