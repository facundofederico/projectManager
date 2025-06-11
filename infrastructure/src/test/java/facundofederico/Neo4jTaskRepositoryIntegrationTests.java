package facundofederico;

import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.time.Duration;
import java.util.HashSet;
import static org.junit.Assert.*;

public class Neo4jTaskRepositoryIntegrationTests {
    private DriverProvider _driverProvider;

    @Before
    public void setUp() {
        _driverProvider = new Neo4jMockDriverProvider();
    }

    @After
    public void tearDown() {
        _driverProvider.closeConnection();
    }

    @Test
    public void neo4jTaskRepository_crud() throws TaskNotFoundException {
        // Arrange
        var repository = new Neo4jTaskRepository(_driverProvider);

        // Act
        String projectName = "project";
        String projectDescription = "description";
        repository.createProject(new Task(projectName, projectDescription, Duration.ZERO, new HashSet<>()));

        // Assert
        var projects = repository.getProjects();
        assertEquals(1, projects.size());
        assertEquals(projectName, projects.getFirst().getName());
        assertEquals(projectDescription, projects.getFirst().getDescription());
        assertEquals(Duration.ZERO, projects.getFirst().getDuration());

        // Act
        String newDescription = "new description";
        repository.updateTask(new Task(projectName, newDescription, Duration.ZERO, new HashSet<>()));

        // Assert
        projects = repository.getProjects();
        assertEquals(1, projects.size());
        assertEquals(projectName, projects.getFirst().getName());
        assertEquals(newDescription, projects.getFirst().getDescription());
        assertEquals(Duration.ZERO, projects.getFirst().getDuration());

        // Act
        repository.deleteTask(projectName);

        // Assert
        projects = repository.getProjects();
        assertTrue(projects.isEmpty());
    }

    @Test
    public void neo4jTaskRepository_duplicateNames_shouldThrow() {
        // Arrange
        var repository = new Neo4jTaskRepository(_driverProvider);

        // Act
        String projectName = "project";
        String projectDescription = "description";
        repository.createProject(new Task(projectName, projectDescription, Duration.ZERO, new HashSet<>()));

        assertThrows(TaskAlreadyExistsException.class, () -> repository.createProject(new Task(projectName, projectDescription, Duration.ZERO, new HashSet<>())));
    }

    @Test
    public void neo4jTaskRepository_getInexistentName_shouldThrow() {
        // Arrange
        var repository = new Neo4jTaskRepository(_driverProvider);

        // Assert
        String projectName = "project";
        assertThrows(TaskNotFoundException.class, () -> repository.getTask(projectName));
    }

    @Test
    public void neo4jTaskRepository_createHierarchy() throws TaskNotFoundException {
        // Arrange
        var repository = new Neo4jTaskRepository(_driverProvider);

        // Act
        String projectName = "project";
        String projectDescription = "project description";
        repository.createProject(new Task(projectName, projectDescription, Duration.ZERO, new HashSet<>()));

        String taskAName = "taskA";
        Duration taskADuration = Duration.ofSeconds(60);
        repository.createTask(new Task(taskAName, "", taskADuration, new HashSet<>()), projectName);

        String taskBName = "taskB";
        Duration taskBDuration = Duration.ofSeconds(50);
        repository.createTask(new Task(taskBName, "", taskBDuration, new HashSet<>()), projectName);

        String taskCName = "taskC";
        Duration taskCDuration = Duration.ofSeconds(25);
        repository.createTask(new Task(taskCName, "", taskCDuration, new HashSet<>()), taskBName);

        // Assert
        var project = repository.getTask(projectName);
        assertEquals(projectName, project.getName());
        assertEquals(Duration.ZERO, project.getDuration());
        assertEquals(Duration.ofSeconds(75), project.getTotalDuration());

        var taskB = project.getSubtasks().stream().filter(x -> x.getName().equals(taskBName)).findFirst();
        assertTrue(taskB.isPresent());
        assertEquals(taskBName, taskB.get().getName());
        assertEquals(taskBDuration, taskB.get().getDuration());
        assertEquals(Duration.ofSeconds(75), taskB.get().getTotalDuration());
    }
}
