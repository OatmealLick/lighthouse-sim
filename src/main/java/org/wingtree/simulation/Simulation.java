package org.wingtree.simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.wingtree.beans.*;

import java.util.TimerTask;

@Component
public class Simulation extends TimerTask
{
    private ApplicationContext applicationContext;

    @Autowired
    public Simulation(final ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run()
    {
        final SimulationState simulationState = applicationContext.getBean(SimulationState.class);

        simulationState.getActors().forEach(actor -> updateActor(actor, simulationState));
        simulationState.getRoute().getJunctions().forEach(junction ->
                junction.getTrackingDevices().forEach(trackingDevice ->
                        trackingDevice.updateState(junction.getCoords(), simulationState.getActors())));

        simulationState.getActors().forEach(System.out::println);
        // FIXME this was actually causing the StackOverflowException ??
        // simulationState.getRoute().getJunctions().forEach(junction -> junction.getTrackingDevices().forEach(System.out::println));
    }

    public void updateActor(final InternalActor actor, final SimulationState simulationState)
    {
        final double distanceToCover = calculateDistanceToCover(actor, simulationState.getIntervalInMillis());
        final double distanceToTarget = calculateDistanceBetween(actor.getCurrentCoords(), actor.getTargetCoords());

        final double distance;
        if (distanceToCover > distanceToTarget) {
            setActorOnTarget(actor, simulationState);
            distance = distanceToCover - distanceToTarget;
        }
        else {
            distance = distanceToCover;
        }

        final double degrees = calculateDegrees(actor);
        final double newX = roundToTwoDecimalPlaces(actor.getCurrentCoords().getX() + distance * Math.cos(degrees));
        final double newY = roundToTwoDecimalPlaces(actor.getCurrentCoords().getY() + distance * Math.sin(degrees));
        actor.setCurrentCoords(ImmutableCoords.of(newX, newY));
    }

    private double roundToTwoDecimalPlaces(double number)
    {
        return Math.round(number * 100.0) / 100.0;
    }

    private double calculateDistanceToCover(final InternalActor actor, final long intervalInMillis)
    {
        return actor.getVelocityInMetersPerSec() * intervalInMillis / 1000;
    }

    private double calculateDistanceBetween(Coords currentCoords, Coords targetCoords)
    {
        return Math.sqrt(Math.pow(targetCoords.getX() - currentCoords.getX(), 2)
                + Math.pow(targetCoords.getY() - currentCoords.getY(), 2));
    }

    private void setActorOnTarget(InternalActor actor, SimulationState simulationState)
    {
        actor.setCurrentCoords(actor.getTargetCoords());
        Junction nextTarget = simulationState.getRoute().getNextRandomizedTarget(actor.getTarget());
        actor.setTarget(nextTarget);
    }

    private double calculateDegrees(final InternalActor actor)
    {
        final double distanceX = actor.getTargetCoords().getX() - actor.getCurrentCoords().getX();
        final double distanceY = actor.getTargetCoords().getY() - actor.getCurrentCoords().getY();

        return Math.atan2(distanceY, distanceX);
    }
}
