package org.wingtree.database;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Profile("embedded-neo4j")
@EnableConfigurationProperties
public class DefaultEmbeddedNeo4JDriverProvider implements EmbeddedNeo4jDriverProvider
{
    //todo move to properties
    private static final String ID = "node_id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String RADIUS = "radius";
    private static final Label LANTERN_LABEL = Label.label("lantern");
    private static final Label CAMERA_LABEL = Label.label("camera");
    private static final Label MOVEMENT_SENSOR_LABEL = Label.label("movement-sensor");
    private static final Label VELOCITY_AND_DIRECTION_SENSOR_LABEL = Label.label("velocity-and-direction-sensor");
    private static final RelationshipType CONNECTED_TO = RelationshipType.withName("connected_to");
    private static final RelationshipType HAS = RelationshipType.withName("has");

    private final GraphDatabaseService graphService;

    @Autowired
    public DefaultEmbeddedNeo4JDriverProvider(final Environment environment)
    {
        final File databaseFile = new File(checkNotNull((environment.getProperty("neo4j.path"))));
        graphService = new GraphDatabaseFactory().newEmbeddedDatabase(databaseFile);
        final Thread hook = new Thread(() -> {
            graphService.shutdown();
            databaseFile.delete();
        });
        Runtime.getRuntime().addShutdownHook(hook);

        try (final Transaction transaction = graphService.beginTx()) {
            final Node one = graphService.createNode();
            one.addLabel(LANTERN_LABEL);
            one.setProperty(X, 0D);
            one.setProperty(Y, 0D);
            one.setProperty(ID, "1");
            final Node two = graphService.createNode();
            two.addLabel(LANTERN_LABEL);
            two.setProperty(X, 0D);
            two.setProperty(Y, 10D);
            two.setProperty(ID, "2");
            final Node three = graphService.createNode();
            three.addLabel(LANTERN_LABEL);
            three.setProperty(X, 10D);
            three.setProperty(Y, 0D);
            three.setProperty(ID, "3");

            final Node camera = graphService.createNode();
            camera.addLabel(CAMERA_LABEL);
            camera.setProperty(ID, "1");
            camera.setProperty(RADIUS, 4.0D);
            final Node movementSensor = graphService.createNode();
            movementSensor.addLabel(MOVEMENT_SENSOR_LABEL);
            movementSensor.setProperty(ID, "1");
            movementSensor.setProperty(RADIUS, 5.0D);
            final Node velocityAndDirectionSensor = graphService.createNode();
            velocityAndDirectionSensor.addLabel(VELOCITY_AND_DIRECTION_SENSOR_LABEL);
            velocityAndDirectionSensor.setProperty(ID, "1");
            velocityAndDirectionSensor.setProperty(RADIUS, 6.0D);

            one.createRelationshipTo(two, CONNECTED_TO);
            two.createRelationshipTo(three, CONNECTED_TO);
            three.createRelationshipTo(one, CONNECTED_TO);

            one.createRelationshipTo(camera, HAS);
            one.createRelationshipTo(movementSensor, HAS);
            one.createRelationshipTo(velocityAndDirectionSensor, HAS);
            two.createRelationshipTo(camera, HAS);
            two.createRelationshipTo(movementSensor, HAS);
            two.createRelationshipTo(velocityAndDirectionSensor, HAS);
            three.createRelationshipTo(camera, HAS);
            three.createRelationshipTo(movementSensor, HAS);
            three.createRelationshipTo(velocityAndDirectionSensor, HAS);

            transaction.success();
        }
    }

    @Override
    public GraphDatabaseService get()
    {
        return graphService;
    }
}
