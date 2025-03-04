package org.linx.cli;

import org.linx.exception.ProjectNotFoundException;
import org.linx.service.ProjectService;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(name = "search", description = "Search for a file")
public class SearchCommand implements Runnable {

    ProjectService projectService = new ProjectService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = false, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-q", "--query"}, required = true, description = "File name")
    private String fileName;

    @Override
    public void run() {

        List<String> files = new ArrayList<>();

        if (projectName != null) {
            try {
                files = projectService.getFilesFromProject(projectName);
            } catch (ProjectNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error searching for file: " + e.getMessage());
            }
        }

        List<String> projects = projectService.getProjects();
        for (String project : projects) {
            try {
                files = projectService.getFilesFromProject(project);
            } catch (Exception e) {
                System.out.println("Error searching for file: " + e.getMessage());
            }
        }
    }
}
