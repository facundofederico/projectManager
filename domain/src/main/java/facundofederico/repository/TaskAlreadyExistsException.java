package facundofederico.repository;

public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
