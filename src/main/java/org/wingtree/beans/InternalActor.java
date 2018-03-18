package org.wingtree.beans;

import java.util.Objects;
import java.util.Optional;

public class InternalActor
{
    private final ActorType type;
    private final float velocity;
    private final Optional<String> id;
    private Coords currentCoords;
    private Coords targetCoords;

    InternalActor(final ActorType type,
                  final float velocity,
                  final Optional<String> id,
                  final Coords currentCoords,
                  final Coords targetCoords)
    {
        this.type = type;
        this.velocity = velocity;
        this.id = id;
        this.currentCoords = currentCoords;
        this.targetCoords = targetCoords;
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

    public Coords getTargetCoords()
    {
        return targetCoords;
    }

    public void setCurrentCoords(final Coords currentCoords)
    {
        this.currentCoords = currentCoords;
    }

    public void setTargetCoords(final Coords targetCoords)
    {
        this.targetCoords = targetCoords;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final InternalActor that = (InternalActor) o;
        return Float.compare(that.velocity, velocity) == 0 &&
                type == that.type &&
                Objects.equals(id, that.id) &&
                Objects.equals(currentCoords, that.currentCoords) &&
                Objects.equals(targetCoords, that.targetCoords);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, velocity, id, currentCoords, targetCoords);
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
