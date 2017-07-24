package com.deadvikingstudios.norsetown.model.lighting;

import org.lwjgl.util.vector.Vector3f;

public class SpotLight
{
    private Vector3f color;
    private Vector3f position;
    private float intensity;

    public SpotLight(Vector3f color, Vector3f position, float intensity)
    {
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    public Vector3f getColor()
    {
        return color;
    }

    public void setColor(Vector3f color)
    {
        this.color = color;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }
}
