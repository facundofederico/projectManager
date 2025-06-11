package facundofederico.repository;

import facundofederico.Task;
import java.util.List;

public interface TaskRepository {
    public List<Task> getProjects();
    public Task getTask(String name) throws TaskNotFoundException;
    public void createTask(Task task, String parentName) throws TaskAlreadyExistsException, TaskNotFoundException;
    public void createProject(Task task) throws TaskAlreadyExistsException;
    public void updateTask(Task task) throws TaskNotFoundException;
    public void deleteTask(String name) throws TaskNotFoundException;
}
