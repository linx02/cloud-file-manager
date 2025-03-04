package org.linx.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "list", description = "List files in a project")
public class ListCommand implements Runnable {
    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @Override
    public void run() {
        System.out.println("Listing files in project " + projectName);
    }
}
