package org.wingtree.beans;

import java.util.Objects;
import java.util.Optional;

public class InternalActor
{
    private final ActorType type;
    private final float velocity;
    private final Optional<String> id;
    private Coords currentCoords;
    private Junction target;

    InternalActor(final ActorType type,
                  final float velocity,
                  final Optional<String> id,
                  final Coords currentCoords,
                  final Junction target)
    {
        this.type = type;
        this.velocity = velocity;
        this.id = id;
        this.currentCoords = currentCoords;
        this.target = target;
    }

    public ActorType getType()
    {
        return type;
    }

    public float getVelocity()
    {
        return velocity;
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
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalActor that = (InternalActor) o;
        return Float.compare(that.velocity, velocity) == 0 &&
                type == that.type &&
                Objects.equals(id, that.id) &&
                Objects.equals(currentCoords, that.currentCoords) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, velocity, id, currentCoords, target);
    }

    @Override
    public String toString()
    {
        return "InternalActor{" +
                "type=" + type +
                ", velocity=" + velocity +
                ", id=" + id +
                ", currentCoords=" + currentCoords +
                ", targetCoords=" + targetCoords +
                '}';
    }
}
