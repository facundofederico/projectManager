package facundofederico.commands.project;

import com.google.inject.Inject;
import facundofederico.controller.CliTaskController;
import facundofederico.repository.TaskAlreadyExistsException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "create", description = "Create a new project")
public class ProjectCreateCommand implements Runnable {
    @Option(names = {"-n", "--name"}, description = "Name for the project (must be unique)", required = true)
    String name;

    @Option(names = {"-d", "--description"}, description = "Description for the project", required = true)
    String description;

    @Inject
    CliTaskController _controller;

    @Override
    public void run() {
        System.out.println(String.format("Creating project with name '%s'...", name));

        try {
            _controller.createProject(name, description);
            System.out.println("Project created");
        }
        catch (TaskAlreadyExistsException e) {
            System.out.println(String.format("Project with name '%s' already exists", name));
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

