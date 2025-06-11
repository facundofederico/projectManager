package facundofederico;

import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;
import facundofederico.repository.TaskRepository;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

public class TaskService {
    private final TaskRepository _taskRepository;
    public TaskService(TaskRepository taskRepository){
        _taskRepository = taskRepository;
    }

    public void createProject(String name, String description, Duration duration) throws TaskAlreadyExistsException {
        var task = new Task(name, description, duration, new HashSet<>());
        _taskRepository.createProject(task);
    }

    public void createTask(String parentName, String name, String description, Duration duration) throws TaskAlreadyExistsException, TaskNotFoundException {
        var task = new Task(name, description, duration, new HashSet<>());
        _taskRepository.createTask(task, parentName);
    }

    public List<Task> getProjects() {
        return _taskRepository.getProjects();
    }

    public Task getTask(String name) throws TaskNotFoundException{
        return _taskRepository.getTask(name);
    }

    public void updateTask(String name, String description, Duration duration) throws TaskNotFoundException{
        _taskRepository.updateTask(new Task(name, description, duration, new HashSet<>()));
    }

    public void deleteTask(String name) throws TaskNotFoundException{
        _taskRepository.deleteTask(name);
    }
}
