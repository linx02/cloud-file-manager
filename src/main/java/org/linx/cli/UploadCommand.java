package org.linx.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "upload", description = "Upload a file")
public class UploadCommand implements Runnable {
    @CommandLine.Option(names = {"-p" , "--project"}, required = true, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "File name")
    private String fileName;

    @CommandLine.Option(names = {"-d", "--destination"}, required = false, description = "Destination")
    private String destination;

    @Override
    public void run() {
        System.out.println("Uploading file " + fileName + " to project " + projectName);
    }
}
