package facundofederico.controller;

import facundofederico.Task;

import java.time.Duration;
import java.util.List;

public record TaskDto(String name, String description, Duration duration, Duration totalDuration, List<String> subtasks) {

    public static TaskDto fromTask(Task task){
        return new TaskDto(
                task.getName(),
                task.getDescription(),
                task.getDuration(),
                task.getTotalDuration(),
                task.getSubtasks().stream().map(Task::getName).toList());
    }
}
