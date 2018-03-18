package org.wingtree.beans;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class InternalActorBuilder
{
    private ActorType type;
    private float velocity;
    private Optional<String> id;
    private Coords currentCoords;
    private Coords targetCoords;

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

    public InternalActorBuilder withVelocity(final float velocity)
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

    public InternalActorBuilder withTargetCoords(final Coords targetCoords)
    {
        this.targetCoords = targetCoords;
        return this;
    }

    public InternalActor build()
    {
        checkNotNull(currentCoords);
        checkNotNull(targetCoords);
        if(id == null) {
            id = Optional.empty();
        }
        return new InternalActor(type, velocity, id, currentCoords, targetCoords);
    }
}
