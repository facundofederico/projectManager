package facundofederico.driverprovider;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import facundofederico.Neo4jConfig;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.io.File;
import java.io.IOException;

public class Neo4jDriverProvider implements DriverProvider {
    private final Driver _driver;

    public Neo4jDriverProvider(String configPath) throws ConfigLoadException {
        var config = loadConfig(configPath);
        _driver = GraphDatabase.driver(config.getUri(), AuthTokens.basic(config.getUser(), config.getPassword()));
        _driver.verifyConnectivity();
    }

    @Override
    public Driver getDriver() { return _driver; }

    @Override
    public void verifyConnectivity() { _driver.verifyConnectivity(); }

    @Override
    public void closeConnection() { _driver.close(); }

    private Neo4jConfig loadConfig(String path) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(path), Neo4jConfig.class);
        } catch (StreamReadException e) {
            throw new ConfigLoadException("Malformed JSON in config file: " + path);
        } catch (DatabindException e) {
            throw new ConfigLoadException("JSON structure doesn't match Neo4jConfig class: " + path);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load config file at: " + path);
        }
    }
}
