package org.linx.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import org.linx.cli.*;

@Command(
        name = "cloud-file-manager",
        description = "CLI tool for managing cloud file storage",
        mixinStandardHelpOptions = true,
        subcommands = {
                CreateProjectCommand.class,
                UploadCommand.class,
                DownloadCommand.class,
                ListCommand.class,
                SearchCommand.class,
                DeleteCommand.class
        }
)
public class CommandLineApp implements Runnable {

    public void run() {
        System.out.println("Welcome to Cloud Storage CLI. Use --help for commands.");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CommandLineApp()).execute(args);
        System.exit(exitCode);
    }
}