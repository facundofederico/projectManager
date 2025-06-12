package facundofederico.main.commands.tasks;

import com.google.inject.Inject;
import facundofederico.controller.CliTaskController;
import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.time.Duration;

@Command(name = "create", description = "Create a new task")
public class TaskCreateCommand implements Runnable {
    @Option(names = {"-n", "--name"}, description = "Name for the task (must be unique)", required = true)
    String name;

    @Option(names = {"-d", "--description"}, description = "Description for the task")
    String description;

    @Option(names = {"-p", "--parent"}, description = "Task parent's name")
    String parent;

    @Inject
    CliTaskController _controller;

    @Override
    public void run() {
        System.out.println(String.format("Creating task with name '%s'...", name));

        try {
            _controller.createTask(parent, name, description, Duration.ofDays(3));
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

