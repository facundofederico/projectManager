package facundofederico.commands.project;

import com.google.inject.Inject;
import facundofederico.controller.CliTaskController;
import facundofederico.services.Utils;
import facundofederico.repository.TaskNotFoundException;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "open", description = "Open project")
public class ProjectOpenCommand implements Runnable {
    @CommandLine.Option(names = {"-n", "--name"}, description = "Name of the project", required = true)
    String name;

    @Inject
    CliTaskController _controller;

    @Override
    public void run() {
        try {
            var project = _controller.getProject(name);
            System.out.println("NAME: " + project.name());
            System.out.println("DESCRIPTION: " + project.description());
            System.out.println("TOTAL DURATION: " + Utils.getFormattedDuration(project.totalDuration()));
            System.out.println();
            System.out.println("SUBTASKS");

            if (project.subtasks().isEmpty()) {
                System.out.println("This project has no tasks yet");
                return;
            }

            for (String task : project.subtasks()) {
                System.out.println("- " + task);
            }
            System.out.println("---------------------");
        }
        catch (TaskNotFoundException e) {
            System.out.println(String.format("Project with name '%s' was not found", name));
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

