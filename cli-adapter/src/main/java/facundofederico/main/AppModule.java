package facundofederico.main;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import facundofederico.Neo4jConfig;
import facundofederico.Neo4jTaskRepository;
import facundofederico.TaskService;
import facundofederico.controller.CliTaskController;
import facundofederico.driverprovider.Neo4jDriverProvider;
import facundofederico.services.InputService;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        var driverProvider = connectToNeo4j();
        var repository = new Neo4jTaskRepository(driverProvider);
        var taskService = new TaskService(repository);
        var controller = new CliTaskController(taskService);

        bind(CliTaskController.class).toInstance(controller);
        bind(InputService.class).in(Singleton.class);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            driverProvider.closeConnection();
            System.out.println("Closed connection with neo4j");
        }));
    }

    private Neo4jDriverProvider connectToNeo4j() {
        var env = System.getenv();
        var neo4jConfig = new Neo4jConfig(env.get("NEO4J_URI"), env.get("NEO4J_USER"), env.get("NEO4J_PASSWORD"));

        System.out.println("Attempting to connect to neo4j");

        int maxTries = 3;
        int tries = 0;
        while (tries < maxTries) {
            try {
                var driver = new Neo4jDriverProvider(neo4jConfig);
                driver.verifyConnectivity();
                System.out.println("Connected to neo4j database at " + neo4jConfig.uri());

                return driver;
            } catch (Exception e) {
                tries++;
                var secondsToWait = (int)Math.pow(3, tries);
                System.out.println("Failed to connect to neo4j. Attempting again in " + secondsToWait + " seconds.");

                try {
                    Thread.sleep(1000L * secondsToWait);
                } catch (InterruptedException ignored) { }
            }
        }

        throw new RuntimeException("Cannot connect to Neo4j after " + maxTries + " retries");
    }
}
