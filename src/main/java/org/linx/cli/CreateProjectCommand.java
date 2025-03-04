package org.linx.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "create-project", description = "Create a new project")
public class CreateProjectCommand implements Runnable {
    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @Override
    public void run() {
        System.out.println("Creating project " + projectName);
    }
}
