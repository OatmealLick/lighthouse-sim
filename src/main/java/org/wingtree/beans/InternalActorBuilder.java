package org.wingtree.beans;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class InternalActorBuilder
{
    private ActorType type;
    private double velocity;
    private Optional<String> id;
    private Coords currentCoords;
    private Coords previousCoords;
    private Junction target;

    private InternalActorBuilder()
    {
    }

    public static InternalActorBuilder builder()
    {
        return new InternalActorBuilder();
    }

    public InternalActorBuilder withType(final ActorType type)
    {
        this.type = type;
        return this;
    }

    public InternalActorBuilder withVelocity(final double velocity)
    {
        this.velocity = velocity;
        return this;
    }

    public InternalActorBuilder withId(final Optional<String> id)
    {
        this.id = id;
        return this;
    }

    public InternalActorBuilder withCurrentCoords(final Coords currentCoords)
    {
        this.currentCoords = currentCoords;
        return this;
    }

    public InternalActorBuilder withPreviousCoords(final Coords previousCoords)
    {
        this.previousCoords = previousCoords;
        return this;
    }

    public InternalActorBuilder withTarget(final Junction target)
    {
        this.target = target;
        return this;
    }

    public InternalActor build()
    {
        checkNotNull(type);
        checkState(velocity > 0);
        checkNotNull(currentCoords);
        checkNotNull(previousCoords);
        checkNotNull(target);
        if(id == null) {
            id = Optional.empty();
        }
        return new InternalActor(type, velocity, id, currentCoords, previousCoords, target);
    }
}
