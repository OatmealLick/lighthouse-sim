package org.wingtree.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wingtree.beans.InternalActor;
import org.wingtree.beans.Junction;
import org.wingtree.beans.SimulationState;
import org.wingtree.beans.TrackingDevice;

/**
 * Handles http requests and provides data about simulation for user.
 */
@RestController
public class SimulationController
{
    private ApplicationContext applicationContext;

    @Autowired
    public SimulationController(final ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    @RequestMapping("/lanterns/{lanternId}")
    public Junction getLantern(@PathVariable final String lanternId)
    {
        final SimulationState simulationState = applicationContext.getBean(SimulationState.class);
        try {
            return simulationState.getRoute().getJunctionOfId(lanternId);
        } catch (Exception e) {
            throw new NotFoundException(String.format("Lantern of id %s not found", lanternId));
        }
    }

    @RequestMapping("/cameras/{cameraId}")
    public TrackingDevice getCamera(@PathVariable final String cameraId)
    {
        final SimulationState simulationState = applicationContext.getBean(SimulationState.class);
        try {
            return simulationState.getRoute().getCameraOfId(cameraId);
        } catch (Exception e) {
            throw new NotFoundException(String.format("Camera of id %s not found", cameraId));
        }
    }

    @RequestMapping("/mov-sensors/{id}")
    public TrackingDevice getMovementSensor(@PathVariable final String id)
    {
        final SimulationState simulationState = applicationContext.getBean(SimulationState.class);
        try {
            return simulationState.getRoute().getMovementSensorOfId(id);
        } catch (Exception e) {
            throw new NotFoundException(String.format("Movement sensor of id %s not found", id));
        }
    }

    @RequestMapping("/vel-dir-sensors/{id}")
    public TrackingDevice getVelocityAndDirectionSensor(@PathVariable final String id)
    {
        final SimulationState simulationState = applicationContext.getBean(SimulationState.class);
        try {
            return simulationState.getRoute().getVelocityAndDirectionSensorOfId(id);
        } catch (Exception e) {
            throw new NotFoundException(String.format("Velocity and direction sensor of id %s not found", id));
        }
    }

    @RequestMapping("/actors/{id}")
    public InternalActor getActor(@PathVariable final String id)
    {
        final SimulationState simulationState = applicationContext.getBean(SimulationState.class);
        return simulationState.getActors().stream()
                .filter(actor -> actor.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Actor of id %s not found", id)));
    }
}
