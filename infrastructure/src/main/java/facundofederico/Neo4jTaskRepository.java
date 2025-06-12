package facundofederico;

import facundofederico.driverprovider.DriverProvider;
import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;
import facundofederico.repository.TaskRepository;
import org.neo4j.driver.*;
import java.time.Duration;
import java.util.*;

public class Neo4jTaskRepository implements TaskRepository {
    private final Driver neo4jDriver;
    private GraphBuilder _graphBuilderCache;
    public static final List<String> CONSTRAINTS_QUERIES = List.of(
        "CREATE CONSTRAINT task_name_uniqueness FOR (t:Task) REQUIRE t.name IS UNIQUE"
//        "CREATE CONSTRAINT task_name_existence FOR (t:Task) REQUIRE t.name IS NOT NULL"
    );

    public Neo4jTaskRepository(DriverProvider driverFactory){
        neo4jDriver = driverFactory.getDriver();
    }

    @Override
    public List<Task> getProjects() {
        try (Session session = neo4jDriver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(GET_PROJECTS_QUERY);
                return result.stream().map(record ->
                        new Task(record.get("name").asString(),
                                record.get("description").asString(),
                                getDuration(record),
                                new HashSet<>())).toList();
            });
        }
    }

    @Override
    public Task getTask(String name) throws TaskNotFoundException {
        if (_graphBuilderCache != null && _graphBuilderCache.isNodeLoaded(name)){
            // If the required task is loaded, it means that all the subtasks are loaded too
            return _graphBuilderCache.build(name);
        }

        try (Session session = neo4jDriver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(GET_TASK_BY_ID_QUERY, Values.parameters("name", name));
                var records = result.list();
                if (records.isEmpty())
                    throw new RuntimeException("TaskNotFoundException");

                Map<String, TempNode> tempMap = new HashMap<>();
                for (var row : records) {
                    var newNode = new TempNode(
                            row.get("name").asString(),
                            row.get("description").asString(),
                            getDuration(row),
                            row.get("children").asList(Value::asString));
                    tempMap.put(newNode.name, newNode);
                }

                _graphBuilderCache = new GraphBuilder(tempMap);

                return _graphBuilderCache.build(name);
            });
        }
        catch (RuntimeException e){
            if (e.getMessage().equals("TaskNotFoundException"))
                throw new TaskNotFoundException(String.format("No task with the name '%s' was found.", name));
            throw e;
        }
    }

    @Override
    public void createTask(Task task, String parentName) throws TaskAlreadyExistsException, TaskNotFoundException {
        _graphBuilderCache = null;

        try (Session session = neo4jDriver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                        ADD_TASK_QUERY, Values.parameters(
                                "parentName", parentName,
                                "name", task.getName(),
                                "description", task.getDescription(),
                                "durationInSeconds", task.getDuration().getSeconds())
                ).consume();
                return null;
            });
        }
        catch (org.neo4j.driver.exceptions.ClientException e) {
            switch (e.code()) {
                case "Neo.ClientError.Schema.ConstraintValidationFailed" -> throw new TaskAlreadyExistsException(String.format("A task with the name '%s' already exists.", task.getName()));
                case "Neo.ClientError.General.Forbidden" -> throw new TaskNotFoundException(String.format("No task with the name '%s' was found.", parentName));
                default -> throw e;
            }
        }
    }

    @Override
    public void createProject(Task task) throws TaskAlreadyExistsException {
        try (Session session = neo4jDriver.session()) {
            session.executeWrite(tx -> {
                tx.run(ADD_PROJECT_QUERY, Values.parameters(
                        "name", task.getName(),
                        "description", task.getDescription(),
                        "durationInSeconds", task.getDuration().getSeconds())
                ).consume();
                return null;
            });
        }
        catch (org.neo4j.driver.exceptions.ClientException e) {
            if (e.code().equals("Neo.ClientError.Schema.ConstraintValidationFailed")) {
                throw new TaskAlreadyExistsException(String.format("A project with the name '%s' already exists.", task.getName()));
            } else {
                throw e;
            }
        }
    }

    @Override
    public void updateTask(Task task) throws TaskNotFoundException {
        _graphBuilderCache = null;

        try (Session session = neo4jDriver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    UPDATE_TASK_QUERY, Values.parameters(
                            "name", task.getName(),
                            "description", task.getDescription(),
                            "durationInSeconds", task.getDuration().getSeconds())
                ).consume();
                return null;
            });
        }
        catch (org.neo4j.driver.exceptions.ClientException e) {
            if (e.code().equals("Neo.ClientError.General.Forbidden")) {
                throw new TaskNotFoundException(String.format("No task with the name '%s' was found.", task.getName()));
            } else {
                throw e;
            }
        }
    }

    @Override
    public void deleteTask(String name) throws TaskNotFoundException {
        _graphBuilderCache = null;

        try (Session session = neo4jDriver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                        DELETE_TASK_QUERY, Values.parameters("name", name)
                ).consume();
                return null;
            });
        }
        catch (org.neo4j.driver.exceptions.ClientException e) {
            if (e.code().equals("Neo.ClientError.General.Forbidden") && e.getMessage().equals("ERROR:TASK_NOT_FOUND")) { // "ERROR:TASK_NOT_FOUND" should be in a separate constant
                throw new TaskNotFoundException(String.format("No task with the name '%s' was found.", name));
            } else {
                throw e;
            }
        }
    }

    private Duration getDuration(org.neo4j.driver.Record record){
        var totalSeconds = (record.get("days").asInt(0) * 24 * 60 * 60) + record.get("seconds").asInt(0);
        return Duration.ofSeconds(totalSeconds);
    }

    private final String GET_PROJECTS_QUERY = """
            MATCH (p:Project)
            RETURN p.name AS name, p.description AS description, p.duration.days as days, p.duration.seconds as seconds
            """;

    private final String GET_TASK_BY_ID_QUERY = """
            MATCH path = (root:Task {name: $name})-[:NEEDS*0..]->(descendant)
            WITH root, collect(DISTINCT descendant) AS descendants
            WITH descendants + root AS tasks
            UNWIND tasks AS task
            OPTIONAL MATCH (task)-[:NEEDS]->(child)
            WITH task, collect(child.name) AS childrenNames
            RETURN task.name AS name, task.description AS description, task.duration.days as days, task.duration.seconds as seconds, childrenNames AS children
            """;

    private final String ADD_TASK_QUERY = """
            MATCH (parent:Task { name: $parentName })
            MERGE (task:Task { name: $name })
            SET task.description = $description, task.duration = duration({ seconds: $durationInSeconds })
            MERGE (parent)-[:NEEDS]->(task)
            """;

    private final String ADD_PROJECT_QUERY = """
            CREATE (task:Task:Project { name: $name, description: $description, duration: duration({ seconds: $durationInSeconds }) })
            """;

    private final String UPDATE_TASK_QUERY = """
            MATCH (task:Task { name: $name })
            WITH task
            CALL apoc.util.validate(task IS NULL, "ERROR:TASK_NOT_FOUND", [])
            SET task.description = $description, task.duration = duration({ seconds: $durationInSeconds })
            """;

    private final String DELETE_TASK_QUERY = """
            MATCH (task:Task { name: $name })
            WITH task
            CALL apoc.util.validate(task IS NULL, "ERROR:TASK_NOT_FOUND", [])
            OPTIONAL MATCH (task)-[:NEEDS]->(child:Task)
            WHERE NOT EXISTS {
                MATCH (child)<-[:NEEDS]-(otherTask:Task)
                WHERE otherTask <> task
                RETURN child
                }
            SET child:Orphan
            WITH task
            DETACH DELETE task
            """;

    private record TempNode(String name, String description, Duration duration, List<String> subtasks) {}

    private static class GraphBuilder {
        private final Map<String, TempNode> tempNodes;
        private final Map<String, Task> builtNodes = new HashMap<>();

        public GraphBuilder(Map<String, TempNode> tempNodes) {
            this.tempNodes = tempNodes;
        }

        public Task build(String name) {
            if (builtNodes.containsKey(name))
                return builtNodes.get(name);

            TempNode temp = tempNodes.get(name);

            List<Task> children = temp.subtasks.stream().map(this::build).toList();

            var task = new Task(temp.name, temp.description, temp.duration, new HashSet<>(children));
            builtNodes.put(name, task);

            return task;
        }

        public boolean isNodeLoaded(String name){
            return builtNodes.containsKey(name);
        }
    }
}
