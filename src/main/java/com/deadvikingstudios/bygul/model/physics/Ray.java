package com.deadvikingstudios.bygul.model.physics;

import com.deadvikingstudios.bygul.utils.Maths;
import org.lwjgl.util.vector.Vector3f;

public class Ray
{
    private Vector3f startPoint;
    private Vector3f endPoint;
    private Vector3f direction;

    public Ray(final Vector3f position, final Vector3f direction)
    {
        this.startPoint = new Vector3f(position);
        this.endPoint = new Vector3f(position);
        this.direction = new Vector3f(direction);
    }

    public void step(float scale)
    {
        float yaw = (float) Math.toRadians(this.direction.y +90);
        float pitch = (float) Math.toRadians(this.direction.x);

        this.endPoint.x = (float) (Math.cos(yaw) * scale);
        this.endPoint.z = (float) (Math.sin(yaw) * scale);
        this.endPoint.y = (float) (Math.tan(pitch) * scale);
    }

    public Vector3f getEnd()
    {
        return new Vector3f(this.endPoint);
    }

    public float getLength()
    {
        return Maths.distance(this.startPoint, this.endPoint);
    }
}
