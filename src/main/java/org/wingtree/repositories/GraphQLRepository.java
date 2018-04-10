package org.wingtree.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wingtree.beans.*;

import java.util.Set;

// TODO To be implemented with database connection to GraphQL and Cypher queries

@Component
@Profile("production")
public class GraphQLRepository implements SimulationStateRepository
{
    @Override
    public Route getRoute()
    {
        return null;
    }

    @Override
    public Set<InternalActor> getActors()
    {
        return null;
    }

    @Override
    public Set<Camera> getCameras()
    {
        return null;
    }

    @Override
    public Set<MovementSensor> getMovementSensors()
    {
        return null;
    }

    @Override
    public Set<VelocityAndDirectionSensor> getMovementAndDirectionSensors()
    {
        return null;
    }
}
