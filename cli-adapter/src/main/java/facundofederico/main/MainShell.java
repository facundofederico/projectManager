package facundofederico.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import facundofederico.main.commands.HelpCommand;
import facundofederico.main.commands.MainCommand;
import facundofederico.main.commands.project.ProjectCommand;
import facundofederico.main.services.GuiceFactory;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainShell {
    public static void main(String[] args) {
        CommandLine cmd = setUpCommandLine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Project Planner. Write 'help' to get a list of possible commands.");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();

            if (line.trim().equalsIgnoreCase("exit")) break;

            String[] inputArgs = line.trim().split("\\s+");
            cmd.execute(inputArgs);
        }
    }

    private static CommandLine setUpCommandLine(){
        Injector injector = Guice.createInjector(new AppModule());
        CommandLine.IFactory factory = new GuiceFactory(injector);
        CommandLine cmd = new CommandLine(MainCommand.class, factory);

        cmd.addSubcommand(ProjectCommand.class);
        cmd.addSubcommand(HelpCommand.class);

        return cmd;
    }
}
