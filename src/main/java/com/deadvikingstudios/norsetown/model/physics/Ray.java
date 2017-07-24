package com.deadvikingstudios.norsetown.model.physics;

import org.lwjgl.util.vector.Vector3f;

public class Ray
{
    private float yaw, pitch;
    Vector3f startPoint;
    Vector3f endPoint;

    public Ray(float yaw, float pitch, final Vector3f origin)
    {
        this.yaw = yaw;
        this.pitch = pitch;
        this.startPoint = origin;
        this.endPoint = origin;
    }

    public void step(float scaler)
    {
        endPoint.x -= Math.cos(Math.toRadians(yaw))*scaler;
        endPoint.z -= Math.sin(Math.toRadians(yaw))*scaler;
        endPoint.y -= Math.tan(Math.toRadians(pitch))*scaler;
    }

    public Vector3f getEndPoint()
    {
        return endPoint;
    }

    public double getLength()
    {
        return MathPhysics.getDistance(startPoint, endPoint);
    }

}
