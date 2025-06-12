package facundofederico.commands.tasks;
import picocli.CommandLine.Command;

@Command(name = "task", description = "Parent of task commands", subcommands = {
        TaskCreateCommand.class,
        TaskUpdateCommand.class,
        TaskDeleteCommand.class,
        TaskOpenCommand.class
})
public class TaskCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Use a subcommand like: create, open, etc.");
    }
}

