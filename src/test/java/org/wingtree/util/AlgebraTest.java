package org.wingtree.util;

import org.junit.jupiter.api.Test;
import org.wingtree.beans.Coords;
import org.wingtree.beans.ImmutableCoords;
import org.wingtree.beans.InternalActor;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

class AlgebraTest
{
    @Test
    void targetMovementWithinAngleLimitIsAccepted()
    {
        // given
        double acceptedAngle = 25;
        Coords homeCoords = ImmutableCoords.of(0, 0);
        Coords currentCoords = ImmutableCoords.of(0, 5);
        Coords previousCoords = ImmutableCoords.of(2, 5);
        InternalActor actor = mock(InternalActor.class, withSettings().stubOnly());
        given(actor.getCurrentCoords()).willReturn(currentCoords);
        given(actor.getPreviousCoords()).willReturn(previousCoords);

        // when
        boolean testResult = Algebra.isTargetMovingInParallelDirection(homeCoords, actor, acceptedAngle);

        // then
        assertThat(testResult).isTrue();
    }

    @Test
    void targetMovementExceedingAngleLimitIsDiscarded()
    {
        // given
        double acceptedAngle = 25;
        Coords homeCoords = ImmutableCoords.of(0, 0);
        Coords currentCoords = ImmutableCoords.of(0, 5);
        Coords previousCoords = ImmutableCoords.of(4, 5);
        InternalActor actor = mock(InternalActor.class, withSettings().stubOnly());
        given(actor.getCurrentCoords()).willReturn(currentCoords);
        given(actor.getPreviousCoords()).willReturn(previousCoords);

        // when
        boolean testResult = Algebra.isTargetMovingInParallelDirection(homeCoords, actor, acceptedAngle);

        // then
        assertThat(testResult).isFalse();
    }
}