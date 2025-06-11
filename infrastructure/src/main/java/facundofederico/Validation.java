package facundofederico;

import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Description;

import java.util.List;

public class Validation {
    @Procedure("apoc.util.validate")
    @Description("If the given predicate is true an exception is thrown.")
    public void validate(
            @Name(value = "predicate", description = "The predicate to check against.") boolean predicate,
            @Name(
                    value = "message",
                    description = "The error message to throw if the given predicate evaluates to true.")
            String message,
            @Name(value = "params", description = "The parameters for the given error message.") List<Object> params) {
        if (predicate) {
            if (params != null && !params.isEmpty())
                message = String.format(message, params.toArray(new Object[params.size()]));
            throw new RuntimeException(message);
        }
    }
}
