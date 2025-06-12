package facundofederico.commands.tasks;

import com.google.inject.Inject;
import facundofederico.controller.CliTaskController;
import facundofederico.services.InputService;
import facundofederico.repository.TaskNotFoundException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "delete", description = "Delete a tasks")
public class TaskDeleteCommand implements Runnable {
    @Option(names = {"-n", "--name"}, description = "Name of the project to delete", required = true)
    String name;

    @Inject
    CliTaskController _controller;

    @Inject
    InputService input;

    @Override
    public void run() {
        System.out.println(String.format("Deletion is irreversible. Are you sure you want to delete the task '%s'? (write 'yes' is you want to proceed)", name));
        System.out.print("> ");
        String line = input.nextLine();

        if (!line.equals("yes")){
            System.out.println("Deletion aborted");
            return;
        }

        System.out.println(String.format("Deleting task with name '%s'...", name));

        try {
            _controller.deleteTask(name);
            System.out.println("Task deleted");
        }
        catch (TaskNotFoundException e) {
            System.out.println(String.format("Task with name '%s' was not found", name));
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

