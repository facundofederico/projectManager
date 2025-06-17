package facundofederico.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import facundofederico.commands.MainCommand;
import facundofederico.commands.project.ProjectCommand;
import facundofederico.commands.tasks.TaskCommand;
import facundofederico.services.GuiceFactory;
import facundofederico.services.Utils;
import picocli.CommandLine;
import java.util.Scanner;

public class MainShell {
    public static void main(String[] args) {
        CommandLine cmd = setUpCommandLine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Project Manager. Write '-h' to get a list of possible commands.");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();

            if (line.trim().equalsIgnoreCase("exit")) break;

            String[] inputArgs = Utils.getParsedCommandLine(line);
            cmd.execute(inputArgs);
        }
    }

    private static CommandLine setUpCommandLine(){
        Injector injector = Guice.createInjector(new AppModule());
        CommandLine.IFactory factory = new GuiceFactory(injector);
        CommandLine cmd = new CommandLine(MainCommand.class, factory);

        cmd.addSubcommand(ProjectCommand.class);
        cmd.addSubcommand(TaskCommand.class);

        return cmd;
    }
}
