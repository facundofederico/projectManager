package facundofederico.controller;

import facundofederico.TaskService;
import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;

import java.time.Duration;
import java.util.List;

public class CliTaskController implements TaskController {
    private final TaskService _taskService;

    public CliTaskController(TaskService taskService){
        _taskService = taskService;
    }

    @Override
    public void createProject(String name, String description) throws TaskAlreadyExistsException {
        _taskService.createProject(name, description, Duration.ZERO);
    }

    @Override
    public void updateProject(String name, String description) throws TaskNotFoundException {
        _taskService.updateTask(name, description, Duration.ZERO);
    }

    @Override
    public List<ProjectDto> getProjects() {
        return _taskService.getProjects().stream()
                .map(ProjectDto::fromTask)
                .toList();
    }

    @Override
    public TaskDto getProject(String name) throws TaskNotFoundException {
        var task = _taskService.getTask(name);
        return TaskDto.fromTask(task);
    }

    @Override
    public void deleteProject(String name) throws TaskNotFoundException {
        _taskService.deleteTask(name);
    }

    @Override
    public void createTask(String parentName, String name, String description, Duration duration) throws TaskAlreadyExistsException, TaskNotFoundException {
        _taskService.createTask(parentName, name, description, duration);
    }

    @Override
    public void updateTask(String name, String description, Duration duration) throws TaskNotFoundException {
        _taskService.updateTask(name, description, duration);
    }

    @Override
    public TaskDto getTask(String name) throws TaskNotFoundException {
        var task = _taskService.getTask(name);
        return TaskDto.fromTask(task);
    }

    @Override
    public void deleteTask(String name) throws TaskNotFoundException {
        _taskService.deleteTask(name);
    }
}
