package facundofederico.commands.project;

import com.google.inject.Inject;
import facundofederico.controller.CliTaskController;
import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "update", description = "Update a project's description")
public class ProjectUpdateCommand implements Runnable {
    @Option(names = {"-n", "--name"}, description = "Name of the project to update", required = true)
    String name;

    @Option(names = {"-d", "--description"}, description = "Description for the project", required = true)
    String description;

    @Inject
    CliTaskController _controller;

    @Override
    public void run() {
        System.out.println(String.format("Updating project with name '%s'...", name));

        try {
            _controller.updateProject(name, description);
            System.out.println("Project updated");
        }
        catch (TaskNotFoundException e) {
            System.out.println(String.format("Project with name '%s' was not found", name));
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

