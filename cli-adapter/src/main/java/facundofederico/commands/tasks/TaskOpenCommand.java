package facundofederico.commands.tasks;

import com.google.inject.Inject;
import facundofederico.controller.CliTaskController;
import facundofederico.services.Utils;
import facundofederico.repository.TaskNotFoundException;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "open", description = "Open task")
public class TaskOpenCommand implements Runnable {
    @CommandLine.Option(names = {"-n", "--name"}, description = "Name of the task", required = true)
    String name;

    @Inject
    CliTaskController _controller;

    @Override
    public void run() {
        try {
            var task = _controller.getTask(name);
            System.out.println("NAME: " + task.name());
            System.out.println("DESCRIPTION: " + task.description());
            System.out.println("DURATION: " + Utils.getFormattedDuration(task.duration()));
            System.out.println("TOTAL DURATION: " + Utils.getFormattedDuration(task.totalDuration()));
            System.out.println();
            System.out.println("SUBTASKS");

            if (task.subtasks().isEmpty()) {
                System.out.println("This task has no subtasks yet");
                return;
            }

            for (String subtask : task.subtasks()) {
                System.out.println("- " + subtask);
            }
            System.out.println("---------------------");
        }
        catch (TaskNotFoundException e) {
            System.out.println(String.format("Task with name '%s' was not found", name));
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

