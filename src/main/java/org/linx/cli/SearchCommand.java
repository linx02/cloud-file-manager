package org.linx.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "search", description = "Search for a file")
public class SearchCommand implements Runnable {
    @CommandLine.Option(names = {"-p" , "--project"}, required = false, description = "Project name")
    private String projectName;

    @CommandLine.Option(names = {"-q", "--query"}, required = true, description = "File name")
    private String fileName;

    @Override
    public void run() {
        System.out.println("Searching for file " + fileName);
    }
}
