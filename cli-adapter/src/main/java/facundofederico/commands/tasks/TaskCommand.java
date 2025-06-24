package facundofederico.commands.tasks;
import facundofederico.commands.VersionProvider;
import picocli.CommandLine.Command;

@Command(name = "task", description = "Parent of task commands", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
        TaskCreateCommand.class,
        TaskUpdateCommand.class,
        TaskDeleteCommand.class,
        TaskOpenCommand.class
})
public class TaskCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Use -h to see subcommands");
    }
}

