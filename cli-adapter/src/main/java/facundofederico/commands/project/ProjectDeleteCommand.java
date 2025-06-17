package facundofederico.commands.project;

import com.google.inject.Inject;
import facundofederico.commands.VersionProvider;
import facundofederico.controller.CliTaskController;
import facundofederico.services.InputService;
import facundofederico.repository.TaskNotFoundException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "delete", description = "Delete a project", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class ProjectDeleteCommand implements Runnable {
    @Option(names = {"-n", "--name"}, description = "Name of the project to delete", required = true)
    String name;

    @Inject
    CliTaskController _controller;

    @Inject
    InputService input;

    @Override
    public void run() {
        System.out.println(String.format("Deletion is irreversible. Are you sure you want to delete the project '%s'? (write 'yes' is you want to proceed)", name));
        System.out.print("> ");
        String line = input.nextLine();

        if (!line.equals("yes")){
            System.out.println("Deletion aborted");
            return;
        }

        System.out.println(String.format("Deleting project with name '%s'...", name));

        try {
            _controller.deleteProject(name);
            System.out.println("Project deleted");
        }
        catch (TaskNotFoundException e) {
            System.out.println(String.format("Project with name '%s' was not found", name));
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

