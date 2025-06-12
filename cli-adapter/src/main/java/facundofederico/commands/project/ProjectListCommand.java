package facundofederico.commands.project;

import com.google.inject.Inject;
import facundofederico.controller.CliTaskController;
import facundofederico.controller.ProjectDto;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List projects")
public class ProjectListCommand implements Runnable {
    @Inject
    CliTaskController _controller;

    @Override
    public void run() {
        try {
            var projects = _controller.getProjects();
            if (projects.isEmpty()){
                System.out.println("You have no projects yet");
                return;
            }

            System.out.println("PROJECTS");
            for (ProjectDto project : projects) {
                System.out.println("- " + project.name());
            }
            System.out.println("---------------------");
        }
        catch (Exception e) {
            System.out.println("UnhandledException: " + e.getMessage());
        }
    }
}

