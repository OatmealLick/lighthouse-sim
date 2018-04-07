package org.wingtree.beans;

import java.util.Objects;
import java.util.Optional;

public class InternalActor
{
    private final ActorType type;
    private final double velocityInMetersPerSec;
    private final Optional<String> id;
    private Coords currentCoords;
    private Junction target;

    InternalActor(final ActorType type,
                  final double velocityInMetersPerSec,
                  final Optional<String> id,
                  final Coords currentCoords,
                  final Junction target)
    {
        this.type = type;
        this.velocityInMetersPerSec = velocityInMetersPerSec;
        this.id = id;
        this.currentCoords = currentCoords;
        this.target = target;
    }

    public ActorType getType()
    {
        return type;
    }

    public double getVelocityInMetersPerSec()
    {
        return velocityInMetersPerSec;
    }

    public Optional<String> getId()
    {
        return id;
    }

    public Coords getCurrentCoords()
    {
        return currentCoords;
    }

    public Junction getTarget()
    {
        return target;
    }

    public Coords getTargetCoords()
    {
        return target.getCoords();
    }

    public void setCurrentCoords(final Coords currentCoords)
    {
        this.currentCoords = currentCoords;
    }

    public void setTarget(final Junction target)
    {
        this.target = target;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final InternalActor that = (InternalActor) o;
        return Double.compare(that.velocityInMetersPerSec, velocityInMetersPerSec) == 0 &&
                type == that.type &&
                Objects.equals(id, that.id) &&
                Objects.equals(currentCoords, that.currentCoords) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, velocityInMetersPerSec, id, currentCoords, target);
    }

    @Override
    public String toString()
    {
        return "InternalActor{" +
                "type=" + type +
                ", velocityInMetersPerSec=" + velocityInMetersPerSec +
                ", id=" + id +
                ", currentCoords=" + currentCoords +
                '}';
    }
}
