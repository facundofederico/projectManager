package facundofederico.commands;

import picocli.CommandLine.Command;

@Command(name = "", description = "Interactive CLI", subcommands = {}, mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class MainCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Write a valid command");
    }
}
