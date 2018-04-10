package org.wingtree.beans;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class SimulationStateBuilder
{
    private long intervalInMillis;
    private Route route;
    private Set<InternalActor> actors;
    private Set<Camera> cameras;
    private Set<MovementSensor> movementSensors;
    private Set<VelocityAndDirectionSensor> velocityAndDirectionSensors;

    private SimulationStateBuilder()
    {
    }

    public static SimulationStateBuilder builder()
    {
        return new SimulationStateBuilder();
    }

    public SimulationStateBuilder withIntervalInMillis(final long intervalInMillis)
    {
        this.intervalInMillis = intervalInMillis;
        return this;
    }

    public SimulationStateBuilder withRoute(final Route route)
    {
        this.route = route;
        return this;
    }

    public SimulationStateBuilder withActors(final Set<InternalActor> actors)
    {
        this.actors = actors;
        return this;
    }

    public SimulationStateBuilder withCameras(final Set<Camera> cameras)
    {
        this.cameras = cameras;
        return this;
    }

    public SimulationStateBuilder withMovementSensors(final Set<MovementSensor> movementSensors)
    {
        this.movementSensors = movementSensors;
        return this;
    }

    public SimulationStateBuilder withMovementAndDirectionSensors(final Set<VelocityAndDirectionSensor>
                                                                          velocityAndDirectionSensors)
    {
        this.velocityAndDirectionSensors = velocityAndDirectionSensors;
        return this;
    }

    public SimulationState build()
    {
        checkState(intervalInMillis > 0);
        checkNotNull(route);
        checkNotNull(actors);
        checkNotNull(cameras);
        checkNotNull(movementSensors);
        checkNotNull(velocityAndDirectionSensors);

        return new SimulationState(intervalInMillis,
                                   route,
                                   actors,
                                   cameras,
                                   movementSensors,
                                   velocityAndDirectionSensors);
    }
}
