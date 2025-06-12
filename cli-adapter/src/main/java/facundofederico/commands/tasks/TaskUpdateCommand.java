package facundofederico.commands.tasks;

import com.google.inject.Inject;
import facundofederico.controller.CliTaskController;
import facundofederico.repository.TaskNotFoundException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.time.Duration;

@Command(name = "update", description = "Update a task's description")
public class TaskUpdateCommand implements Runnable {
    @Option(names = {"-n", "--name"}, description = "Name of the task to update", required = true)
    String name;

    @Option(names = {"-d", "--description"}, description = "Description for the task", required = true)
    String description;

    @Inject
    CliTaskController _controller;

    @Override
    public void run() {
        System.out.println(String.format("Updating task with name '%s'...", name));

        try {
            _controller.updateTask(name, description, Duration.ofDays(3));
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

