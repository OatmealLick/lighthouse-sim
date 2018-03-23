package org.wingtree.simulation;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.wingtree.beans.*;

public class Simulation implements Job
{
    @Override
    public void execute(final JobExecutionContext jobExecutionContext)
    {
        // TODO if this is to work as http server this step needs to set some object properties, and this object will
        // be then queried for data. Remember about synchronized IO to this object
        final JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        final long intervalInMillis = dataMap.getLong(SimulationManager.TIME_INTERVAL);
        final StartupParameters startupParameters =
                (StartupParameters) dataMap.get(SimulationManager.STARTUP_PARAMETERS);
        startupParameters.getInternalActors().forEach(actor -> updateActor(actor, intervalInMillis, startupParameters));
        //TODO update cameras
        //TODO update movement sensors
        //TODO update movement and direction sensors
        //TODO what are lanterns for even
        startupParameters.getInternalActors().forEach(System.out::println);
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
        return actor.getVelocity() * interval;
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
