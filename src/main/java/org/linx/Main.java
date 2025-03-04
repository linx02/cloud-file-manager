package org.linx;
import org.linx.cli.*;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new CommandLineApp()).execute(args);
        System.exit(exitCode);
    }
}