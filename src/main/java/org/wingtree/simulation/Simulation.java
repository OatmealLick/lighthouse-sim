package org.wingtree.simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.wingtree.beans.Coords;
import org.wingtree.beans.ImmutableCoords;
import org.wingtree.beans.InternalActor;
import org.wingtree.beans.SimulationState;

import java.util.TimerTask;

@Component
public class Simulation extends TimerTask
{
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run()
    {
        final SimulationState simulationState =
                (SimulationState) applicationContext.getAutowireCapableBeanFactory().getBean("simulation-state");
        final long intervalInMillis =
                (long) applicationContext.getAutowireCapableBeanFactory().getBean("interval-in-millis");

        simulationState.getInternalActors().forEach(actor -> updateActor(actor, intervalInMillis));
        simulationState.getLanterns().forEach(lantern ->
                lantern.getTrackingDevices().forEach(trackingDevice ->
                        trackingDevice.updateState(lantern.getCoords(), simulationState.getInternalActors())));

        simulationState.getInternalActors().forEach(System.out::println);
        simulationState.getLanterns().forEach(lantern -> lantern.getTrackingDevices().forEach(System.out::println));
    }

    private void updateActor(final InternalActor actor, final long intervalInMillis)
    {
        //TODO add behavior for situations when actor reaches the point
        // TODO this below stopped working for some reason
//        final double distance = calculateDistance(actor, intervalInMillis);
//        final double degrees = calculateDegrees(actor);
//        final double newX = distance * Math.cos(degrees);
//        final double newY = distance * Math.sin(degrees);
//        actor.setCurrentCoords(ImmutableCoords.of(newX, newY));

        //todo replace this when the rest works, this only simulates movement to test that actor is updated
        final Coords currentCoords = actor.getCurrentCoords();
        actor.setCurrentCoords(ImmutableCoords.of(currentCoords.getX()*2+intervalInMillis, currentCoords.getY()
                *2+intervalInMillis));
    }

    private double calculateDistance(final InternalActor actor, final long intervalInMillis)
    {
        final Coords coords = actor.getCurrentCoords();
        return Math.sqrt(Math.pow(coords.getY(), 2) + Math.pow(coords.getX(), 2))
                + actor.getVelocityInMetersPerSec() * convertToSeconds(intervalInMillis);
    }

    private double convertToSeconds(final long intervalInMillis)
    {
        return intervalInMillis / 1000;
    }

    private double calculateDegrees(final InternalActor actor)
    {
        final double distanceX = Math.abs(actor.getTargetCoords().getX() - actor.getCurrentCoords().getX());
        final double distanceY = Math.abs(actor.getTargetCoords().getY() - actor.getCurrentCoords().getY());
        return Math.atan(distanceY / distanceX);
    }
}
