package facundofederico.commands;

import picocli.CommandLine.Command;

@Command(name = "help", description = "List of commands", subcommands = {})
public class HelpCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("This program lets you create projects, and a hierarchy of tasks under it. You can start by creating a project and opening it.");
        System.out.println();
        System.out.println("SUPPORTED COMMANDS");
        System.out.println("help");
        System.out.println("exit");
        System.out.println();
        System.out.println("project create -n [name] -d [description]");
        System.out.println("project open -n [name]");
        System.out.println("project update -n [name] -d [description]");
        System.out.println("project delete -n [name]");
        System.out.println("project list");
        System.out.println();
        System.out.println("task create -n [name] -d [description] -p [parent]");
        System.out.println("task open -n [name]");
        System.out.println("task update -n [name] -d [description]");
        System.out.println("task delete -n [name]");
    }
}
