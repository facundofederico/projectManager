package facundofederico.driverprovider;

import facundofederico.Validation;
import org.neo4j.driver.*;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import static facundofederico.Neo4jTaskRepository.CONSTRAINTS_QUERIES;

public class Neo4jInMemoryDriverProvider implements DriverProvider {
    private final Neo4j _server;
    private final Driver _driver;

    public Neo4jInMemoryDriverProvider() {
        _server = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withProcedure(Validation.class)
                .build();
        _driver = GraphDatabase.driver(_server.boltURI(), Config.defaultConfig());
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
    public void closeConnection() {
        _driver.close();
        _server.close();
    }
}
