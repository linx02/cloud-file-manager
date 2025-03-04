package org.linx.cli;

import org.linx.exception.ProjectNotFoundException;
import org.linx.service.ProjectService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "list", description = "List files in a project")
public class ListCommand implements Runnable {

    ProjectService projectService = new ProjectService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @Override
    public void run() {
        try {
            List<String> files = projectService.getFilesFromProject(projectName);
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error listing files: " + e.getMessage());
        }
    }
}
