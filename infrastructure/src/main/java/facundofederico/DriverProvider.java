package facundofederico;

import org.neo4j.driver.Driver;

public interface DriverProvider {
    public Driver getDriver();
    public void verifyConnectivity();
    public void closeConnection();
}
