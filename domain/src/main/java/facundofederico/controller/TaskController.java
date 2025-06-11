package facundofederico.controller;

import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;

import java.time.Duration;
import java.util.List;

public interface TaskController {
    // Projects
    public void createProject(String name, String description) throws TaskAlreadyExistsException;
    public void updateProject(String name, String description) throws TaskNotFoundException;
    public List<ProjectDto> getProjects();
    public TaskDto getProject(String name) throws TaskNotFoundException;
    public void deleteProject(String name) throws TaskNotFoundException;

    // Tasks
    public void createTask(String parentName, String name, String description, Duration duration) throws TaskAlreadyExistsException, TaskNotFoundException;
    public void updateTask(String name, String description, Duration duration) throws TaskNotFoundException;
    public TaskDto getTask(String name) throws TaskNotFoundException;
    public void deleteTask(String name) throws TaskNotFoundException;
}
