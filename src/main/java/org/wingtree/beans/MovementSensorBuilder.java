package org.wingtree.beans;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class MovementSensorBuilder
{
    private String lanternId;
    private boolean sensingMovement = false;
    private double radius = -1;

    public static MovementSensorBuilder builder()
    {
        return new MovementSensorBuilder();
    }

    private MovementSensorBuilder()
    {
    }

    public MovementSensorBuilder withLanternId(final String lanternId)
    {
        this.lanternId = lanternId;
        return this;
    }

    public MovementSensorBuilder withSensingMovement(final boolean sensingMovement)
    {
        this.sensingMovement = sensingMovement;
        return this;
    }

    public MovementSensorBuilder withRadius(final double radius)
    {
        this.radius = radius;
        return this;
    }

    public MovementSensor build()
    {
        checkNotNull(lanternId);
        checkState(radius > 0);
        return new MovementSensor(lanternId, sensingMovement, radius);
    }
}
