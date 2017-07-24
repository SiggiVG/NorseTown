package com.deadvikingstudios.norsetown.model.physics;

import org.lwjgl.util.vector.Vector3f;

public class MathPhysics
{
    public static double getDistance(Vector3f p1, Vector3f p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2) + Math.pow(p2.getZ() - p1.getZ(), 2));
    }
}
