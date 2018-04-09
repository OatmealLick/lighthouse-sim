package org.wingtree.database;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Profile("embedded-neo4j")
@EnableConfigurationProperties
public class EmbeddedGraphDatabaseServiceProvider implements GraphDatabaseServiceProvider {

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

    private final GraphDatabaseService graphService;

    @Autowired
    public EmbeddedGraphDatabaseServiceProvider(final Environment environment)
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
            camera.setProperty(ID, "4");
            camera.setProperty(RADIUS, 0.7D);
            final Node movementSensor = graphService.createNode();
            movementSensor.addLabel(MOVEMENT_SENSOR_LABEL);
            movementSensor.setProperty(ID, "5");
            movementSensor.setProperty(RADIUS, 2.5D);
            movementSensor.setProperty(SENSING_MOVEMENT, false);

            one.createRelationshipTo(two, CONNECTED_TO);
            two.createRelationshipTo(three, CONNECTED_TO);
            three.createRelationshipTo(one, CONNECTED_TO);
            one.createRelationshipTo(camera, HAS);
            two.createRelationshipTo(movementSensor, HAS);

            transaction.success();
        }
    }

    @Override
    public GraphDatabaseService get()
    {
        return graphService;
    }
}
