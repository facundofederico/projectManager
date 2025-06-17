package facundofederico.commands.tasks;

import com.google.inject.Inject;
import facundofederico.commands.VersionProvider;
import facundofederico.controller.CliTaskController;
import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;
import facundofederico.services.Utils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "create", description = "Create a new task", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class TaskCreateCommand implements Runnable {
    @Option(names = {"-n", "--name"}, description = "Name for the task (must be unique)", required = true)
    String name;

    @Option(names = {"-d", "--description"}, description = "Description for the task", required = true)
    String description;

    @Option(names = {"-p", "--parent"}, description = "Task parent's name", required = true)
    String parent;

    @Option(names = {"-dd", "--durationDays"}, description = "Task duration's days component")
    Long durationDays;

    @Option(names = {"-dh", "--durationHours"}, description = "Task duration's hours component")
    Long durationHours;

    @Option(names = {"-dm", "--durationMinutes"}, description = "Task duration's minutes component")
    Long durationMinutes;

    @Option(names = {"-ds", "--durationSeconds"}, description = "Task duration's seconds component")
    Long durationSeconds;

    @Inject
    CliTaskController _controller;

    @Override
    public void run() {
        System.out.println(String.format("Creating task with name '%s'...", name));

        try {
            _controller.createTask(parent, name, description, Utils.getDurationFrom(durationDays, durationHours, durationMinutes, durationSeconds));
            System.out.println("Task created");
        }
        catch (TaskNotFoundException e) {
            System.out.println(String.format("Parent with name '%s' was not found", parent));
        }
        catch (TaskAlreadyExistsException e) {
            System.out.println(String.format("Task with name '%s' already exists", name));
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

