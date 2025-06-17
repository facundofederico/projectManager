package facundofederico.commands.tasks;

import com.google.inject.Inject;
import facundofederico.commands.VersionProvider;
import facundofederico.controller.CliTaskController;
import facundofederico.repository.TaskNotFoundException;
import facundofederico.services.Utils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "update", description = "Update a task's description", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class TaskUpdateCommand implements Runnable {
    @Option(names = {"-n", "--name"}, description = "Name of the task to update", required = true)
    String name;

    @Option(names = {"-d", "--description"}, description = "Description for the task", required = true)
    String description;

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
        System.out.println(String.format("Updating task with name '%s'...", name));

        try {
            _controller.updateTask(name, description, Utils.getDurationFrom(durationDays, durationHours, durationMinutes, durationSeconds));
            System.out.println("Task updated");
        }
        catch (TaskNotFoundException e) {
            System.out.println(String.format("Task with name '%s' was not found", name));
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

