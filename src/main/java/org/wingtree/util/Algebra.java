package org.wingtree.util;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.wingtree.beans.Coords;
import org.wingtree.beans.InternalActor;

/**
 * Static utility class with methods regarding basic algebraic computations.
 */
public class Algebra
{
    public static boolean isTargetInRadius(final Coords homeCoords,
                                           final double radius,
                                           final Coords targetCoords)
    {
        final double distanceBetweenCoords = getAbsoluteDistance(homeCoords, targetCoords);
        return radius >= distanceBetweenCoords;
    }

    public static boolean isTargetMovingInParallelDirection(final Coords homeCoords,
                                                            final InternalActor actor,
                                                            final double acceptedAngle)
    {
        Vector2D previous = new Vector2D(actor.getPreviousCoords().getX() - homeCoords.getX(),
                                         actor.getPreviousCoords().getY() - homeCoords.getY());
        Vector2D current = new Vector2D(actor.getCurrentCoords().getX() - homeCoords.getX(),
                                        actor.getCurrentCoords().getY() - homeCoords.getY());

        return getRelativeAngle(previous, current) <= Math.toRadians(acceptedAngle);
    }

    public static double getAbsoluteDistance(Coords coords1, Coords coords2)
    {
        return Math.sqrt(Math.pow(coords1.getX() - coords2.getX(), 2) + Math.pow(coords1.getY() - coords2.getY(), 2));
    }

    public static double getRelativeAngle(Vector2D vector1, Vector2D vector2)
    {
        if (vector1.getNorm() == 0 || vector2.getNorm() == 0) return 0;
        else {
            double angle = Math.abs(Vector2D.angle(vector1, vector2));
            if (angle > Math.PI) return angle - Math.PI;
            else return angle;
        }
    }
}
