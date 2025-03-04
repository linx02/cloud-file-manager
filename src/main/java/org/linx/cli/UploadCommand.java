package org.linx.cli;

import org.linx.exception.ProjectNotFoundException;
import org.linx.service.ProjectService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "upload", description = "Upload a file")
public class UploadCommand implements Runnable {

    ProjectService projectService = new ProjectService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "File name")
    private String fileName;

    @CommandLine.Option(names = {"-d", "--destination"}, required = false, description = "Destination")
    private String destination;

    @Override
    public void run() {
        try {
            List<String> files = projectService.getFilesFromProject(projectName);

            if (files.contains(fileName)) {
                System.out.println("File already exists in project");
                return;
            }
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }
}
