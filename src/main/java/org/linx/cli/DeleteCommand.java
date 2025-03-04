package org.linx.cli;

import org.linx.exception.ProjectNotFoundException;
import org.linx.service.ProjectService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "delete", description = "Delete a file")
public class DeleteCommand implements Runnable {

    ProjectService projectService = new ProjectService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "File name")
    private String fileName;

    @Override
    public void run() {
        try {
            List<String> files = projectService.getFilesFromProject(projectName);
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
    }
}
