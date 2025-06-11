package facundofederico.controller;

import facundofederico.Task;

public record ProjectDto(String name, String description) {

    public static ProjectDto fromTask(Task task){
        return new ProjectDto(
                task.getName(),
                task.getDescription());
    }
}
