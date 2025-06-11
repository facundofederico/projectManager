package facundofederico;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Task {
    private final String _name;
    private final String _description;
    private final Duration _duration;
    private final Set<Task> _subtasks;
    private Duration _finalDurationCache;

    public Task(String name, String description, Duration duration, Set<Task> subtasks){
        validateName(name);
        validateDescription(description);
        validateDuration(duration);

        _name = name;
        _description = description;
        _duration = duration;
        _subtasks = subtasks;
    }

    private void validateName(String name) {
        if (name == null)
            throw new TaskAttributeException("Tasks must have a name");
        if (name.length() > 50)
            throw new TaskAttributeException("Task names must be 50 characters or less");
        Pattern p = Pattern.compile("[^a-z0-9-_.]", Pattern.CASE_INSENSITIVE);
        if (p.matcher(name).find())
            throw new TaskAttributeException("Task names can only contain letters (A–Z or a–z), numbers (0–9), hyphens (-), underscores (_), or dots (.)");
    }

    private void validateDescription(String description) {
        if (description == null)
            throw new TaskAttributeException("Tasks must have a description");
        if (description.length() > 1000)
            throw new TaskAttributeException("Task descriptions must be 1000 characters or less");
    }

    private void validateDuration(Duration duration) {
        if (duration == null)
            throw new TaskAttributeException("Tasks must have a duration");
        if (duration.isNegative())
            // Unless we account for the use of a time machine, or some creative interpretation of space-time dilation,
            // negative task durations should not be possible. In any case, it's not something I want to include in my
            // scope (for now). I'm open to hear how would you use negative durations in project planning, though ;)
            throw new TaskAttributeException("Task durations cannot be negative");
    }

    public String getName() { return _name; }
    public String getDescription() { return _description; }
    public Duration getDuration() { return _duration; }
    public List<Task> getSubtasks(){ return _subtasks.stream().toList(); }

    public Duration getTotalDuration(){
        if (_finalDurationCache != null)
            return _finalDurationCache;

        var subtasksDuration = _subtasks.stream()
                .map(Task::getTotalDuration)
                .max(Duration::compareTo)
                .orElse(Duration.ZERO);
        _finalDurationCache = _duration.plus(subtasksDuration);

        return _finalDurationCache;
    }
}
