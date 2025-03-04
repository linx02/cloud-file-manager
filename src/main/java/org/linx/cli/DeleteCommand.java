package org.linx.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "delete", description = "Delete a file")
public class DeleteCommand implements Runnable {
    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "File name")
    private String fileName;

    @Override
    public void run() {
        System.out.println("Deleting file " + fileName + " from project " + projectName);
    }
}
