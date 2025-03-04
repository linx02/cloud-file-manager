package org.linx.cli;

import org.linx.service.DatabaseService;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(name = "search", description = "Search for a file")
public class SearchCommand implements Runnable {

    DatabaseService databaseService = new DatabaseService();

    @CommandLine.Option(names = {"-p" , "--project"}, required = false, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-q", "--query"}, required = true, description = "File name")
    private String fileName;

    @Override
    public void run() {

        List<String> files = new ArrayList<>();

        if (projectName != null) {
            try {
                files = databaseService.listFiles(projectName);
            } catch (Exception e) {
                System.out.println("Error searching for file: " + e.getMessage());
            }
        }

        List<String> projects = databaseService.getProjects();
        for (String project : projects) {
            try {
                files = databaseService.listFiles(project);

                // IMPLEMENT ME
            } catch (Exception e) {
                System.out.println("Error searching for file: " + e.getMessage());
            }
        }
    }
}
