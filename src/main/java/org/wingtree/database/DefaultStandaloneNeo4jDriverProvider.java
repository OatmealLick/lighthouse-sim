package org.wingtree.database;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
@EnableConfigurationProperties
public class DefaultStandaloneNeo4jDriverProvider implements StandaloneNeo4jDriverProvider, AutoCloseable
{
    //todo move to properties
    private static final String ID = "id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String RADIUS = "radius";
    private static final String SENSING_MOVEMENT = "sensing_movement";
    private static final Label LANTERN_LABEL = Label.label("lantern");
    private static final Label CAMERA_LABEL = Label.label("camera");
    private static final Label MOVEMENT_SENSOR_LABEL = Label.label("movement-sensor");
    private static final RelationshipType CONNECTED_TO = RelationshipType.withName("connected_to");
    private static final RelationshipType HAS = RelationshipType.withName("has");

    private final Driver driver;

    @Autowired
    public DefaultStandaloneNeo4jDriverProvider(final Environment environment)
    {
        driver = GraphDatabase.driver(buildUri(environment), AuthTokens.basic(
                environment.getProperty("neo4j.user"),
                environment.getProperty("neo4j.password")));
    }

    private String buildUri(final Environment environment)
    {
        return String.format("%s://%s:%s",
                environment.getProperty("neo4j.scheme"),
                environment.getProperty("neo4j.host"),
                environment.getProperty("neo4j.port"));
    }

    @Override
    public Driver get()
    {
        return driver;
    }

    @Override
    public void close()
    {
        driver.close();
    }
}
