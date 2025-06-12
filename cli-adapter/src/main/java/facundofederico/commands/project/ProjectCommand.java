package facundofederico.commands.project;
import picocli.CommandLine.Command;

@Command(name = "project", description = "Parent of project commands", subcommands = {
        ProjectCreateCommand.class,
        ProjectListCommand.class,
        ProjectUpdateCommand.class,
        ProjectDeleteCommand.class,
        ProjectOpenCommand.class
})
public class ProjectCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Use a subcommand like: create, list, etc.");
    }
}

