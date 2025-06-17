package facundofederico.commands.project;
import facundofederico.commands.VersionProvider;
import picocli.CommandLine.Command;

@Command(name = "project", description = "Parent of project commands", mixinStandardHelpOptions = true, versionProvider = VersionProvider .class, subcommands = {
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

