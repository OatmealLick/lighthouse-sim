package org.wingtree.simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.wingtree.beans.*;

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

    public void updateActor(final InternalActor actor, final long interval, final StartupParameters startupParameters)
    {
        final double distanceToCover = calculateDistanceToCover(actor, interval);
        final double distanceToTarget = calculateDistanceBetween(actor.getCurrentCoords(), actor.getTargetCoords());

        final double distance;
        if (distanceToCover > distanceToTarget) {
            setActorOnTarget(actor, startupParameters);
            distance = distanceToCover - distanceToTarget;
        }
        else {
            distance = distanceToCover;
        }

        final double degrees = calculateDegrees(actor);
        final double newX = distance * Math.cos(degrees);
        final double newY = distance * Math.sin(degrees);
        actor.setCurrentCoords(CoordsBuilder.of(newX, newY));
    }

    private double calculateDistanceToCover(final InternalActor actor, final long interval)
    {
        return actor.getVelocityInMetersPerSec() * interval;
    }

    private double calculateDistanceBetween(Coords currentCoords, Coords targetCoords)
    {
        return Math.sqrt(Math.pow(targetCoords.getX() - currentCoords.getX(), 2)
                + Math.pow(targetCoords.getY() - currentCoords.getY(), 2));
    }

    private void setActorOnTarget(InternalActor actor, StartupParameters startupParameters)
    {
        Junction nextTarget = startupParameters.getRoute().getNextRandomizedTarget(actor.getTarget());
        actor.setTarget(nextTarget);
        actor.setCurrentCoords(nextTarget.getCoords());
    }

    private double calculateDegrees(final InternalActor actor)
    {
        final double distanceX = Math.abs(actor.getTargetCoords().getX() - actor.getCurrentCoords().getX());
        final double distanceY = Math.abs(actor.getTargetCoords().getY() - actor.getCurrentCoords().getY());
        return Math.atan(distanceY / distanceX);
    }
}
