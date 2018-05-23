package org.wingtree.beans;

public class Reading
{
    protected final Direction direction;
    protected final double velocity;

    public Reading(final Direction direction, final double velocity)
    {
        this.direction = direction;
        this.velocity = velocity;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public double getVelocity()
    {
        return velocity;
    }
}
