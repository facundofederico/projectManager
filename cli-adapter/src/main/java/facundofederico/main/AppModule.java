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
        var driverProvider = buildNeo4jDriver();
        var repository = new Neo4jTaskRepository(driverProvider);
        var taskService = new TaskService(repository);
        var controller = new CliTaskController(taskService);

        bind(CliTaskController.class).toInstance(controller);
        bind(InputService.class).in(Singleton.class);
    }

    private Neo4jDriverProvider buildNeo4jDriver() {
        var env = System.getenv();
        var neo4jConfig = new Neo4jConfig(env.get("NEO4J_URI"), env.get("NEO4J_USER"), env.get("NEO4J_PASSWORD"));

        System.out.println("Env configuration for neo4j: " + neo4jConfig);

        int retries = 10;
        while (retries > 0) {
            try {
                var driver = new Neo4jDriverProvider(neo4jConfig);
                Runtime.getRuntime().addShutdownHook(new Thread(driver::closeConnection));

                return driver;
            } catch (Exception e) {
                retries--;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) { }
            }
        }

        throw new RuntimeException("Cannot connect to Neo4j after retries");
    }
}
