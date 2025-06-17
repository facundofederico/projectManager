package facundofederico.driverprovider;

import facundofederico.Neo4jConfig;
import org.neo4j.driver.*;

import static facundofederico.Neo4jTaskRepository.CONSTRAINTS_QUERIES;

public class Neo4jDriverProvider implements DriverProvider {
    private final Driver _driver;

    public Neo4jDriverProvider(Neo4jConfig config) throws ConfigLoadException {
        _driver = GraphDatabase.driver(config.uri(), AuthTokens.basic(config.user(), config.password()));
        _driver.verifyConnectivity();
        initializeDb();
    }

    private void initializeDb() {
        for (var query : CONSTRAINTS_QUERIES) {
            try (Session session = _driver.session()) {
                session.executeWrite(tx -> {
                    tx.run(query, Values.parameters() ).consume();
                    return null;
                });
            }
        }
    }

    @Override
    public Driver getDriver() { return _driver; }

    @Override
    public void verifyConnectivity() { _driver.verifyConnectivity(); }

    @Override
    public void closeConnection() { _driver.close(); }
}
