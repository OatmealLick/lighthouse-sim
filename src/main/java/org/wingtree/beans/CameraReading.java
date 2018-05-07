package org.wingtree.beans;

public class CameraReading extends Reading
{
    private String id;
    private ActorType type;

    public CameraReading(final String id, final ActorType type, final Direction direction, final double velocity)
    {
        super(direction, velocity);
        this.id = id;
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public ActorType getType()
    {
        return type;
    }
}
