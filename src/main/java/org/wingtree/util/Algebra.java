package org.wingtree.util;

import org.wingtree.beans.Coords;

//todo should use some algebraic library
public class Algebra
{
    public static boolean isTargetInRadius(final Coords homeCoords,
                                           final double radius,
                                           final Coords targetCoords)
    {
        final double distanceBetweenCoords =
                Math.sqrt(Math.pow(homeCoords.getX() - targetCoords.getX(), 2) +
                        Math.pow(homeCoords.getY() - targetCoords.getY(), 2));
        return radius >= distanceBetweenCoords;
    }
}
