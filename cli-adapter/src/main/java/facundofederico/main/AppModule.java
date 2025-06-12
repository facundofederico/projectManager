package facundofederico.main;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import facundofederico.Neo4jTaskRepository;
import facundofederico.TaskService;
import facundofederico.controller.CliTaskController;
import facundofederico.driverprovider.Neo4jInMemoryDriverProvider;
import facundofederico.main.services.InputService;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        var driver = new Neo4jInMemoryDriverProvider();
        var repository = new Neo4jTaskRepository(driver);
        var taskService = new TaskService(repository);
        var controller = new CliTaskController(taskService);

        bind(CliTaskController.class).toInstance(controller);
        bind(InputService.class).in(Singleton.class);

        Runtime.getRuntime().addShutdownHook(new Thread(driver::closeConnection));
    }
}
