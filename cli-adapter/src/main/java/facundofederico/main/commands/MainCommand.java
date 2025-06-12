package facundofederico.main.commands;

import picocli.CommandLine.Command;

@Command(name = "", description = "Interactive CLI", subcommands = {})
public class MainCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Write a valid command");
    }
}
