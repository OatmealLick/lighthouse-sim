package org.wingtree.simulation;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.wingtree.beans.Coords;
import org.wingtree.beans.CoordsBuilder;
import org.wingtree.beans.InternalActor;
import org.wingtree.beans.StartupParameters;

public class Simulation implements Job
{
    @Override
    public void execute(final JobExecutionContext jobExecutionContext)
    {
        //TODO if this is to work as http server this step needs to set some object properties, and this object will
        // be then queried for data. Remember about synchronized IO to this object
        final JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        final long intervalInMillis = dataMap.getLong(SimulationManager.TIME_INTERVAL);
        final StartupParameters startupParameters =
                (StartupParameters) dataMap.get(SimulationManager.STARTUP_PARAMETERS);
        startupParameters.getInternalActors().forEach(actor -> updateActor(actor, intervalInMillis));
        //TODO update cameras
        //TODO update movement sensors
        //TODO update movement and direction sensors
        //TODO what are lanterns for even
        startupParameters.getInternalActors().forEach(System.out::println);
    }

    public void updateActor(final InternalActor actor, final long interval)
    {
        //TODO add behavior for situations when actor reaches the point
        final double distance = calculateDistance(actor, interval);
        final double degrees = calculateDegrees(actor);
        final double newX = distance * Math.cos(degrees);
        final double newY = distance * Math.sin(degrees);
        actor.setCurrentCoords(CoordsBuilder.of(newX, newY));
    }

    private double calculateDistance(final InternalActor actor, final long interval)
    {
        final Coords coords = actor.getCurrentCoords();
        return Math.sqrt(Math.pow(coords.getY(), 2) + Math.pow(coords.getX(), 2))
                + actor.getVelocity() * interval;
    }

    private double calculateDegrees(final InternalActor actor)
    {
        final double distanceX = Math.abs(actor.getTargetCoords().getX() - actor.getCurrentCoords().getX());
        final double distanceY = Math.abs(actor.getTargetCoords().getY() - actor.getCurrentCoords().getY());
        return Math.atan(distanceY / distanceX);
    }
}
