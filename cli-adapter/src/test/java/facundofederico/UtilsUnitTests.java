package facundofederico;

import facundofederico.driverprovider.DriverProvider;
import facundofederico.driverprovider.Neo4jInMemoryDriverProvider;
import facundofederico.repository.TaskAlreadyExistsException;
import facundofederico.repository.TaskNotFoundException;
import facundofederico.services.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.HashSet;

import static org.junit.Assert.*;

public class UtilsUnitTests {
    @Test
    public void utils_getParsedCommandLine() {
        // Arrange
        var input = "command  -p  \" This is 1 text \" This is not 123";
        var expected = new String[]{ "command", "-p", " This is 1 text ", "This", "is", "not", "123" };

        // Act
        var result = Utils.getParsedCommandLine(input);

        // Assert
        assertArrayEquals(expected, result);
    }
}
