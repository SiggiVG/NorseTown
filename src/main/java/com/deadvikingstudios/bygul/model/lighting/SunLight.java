package com.deadvikingstudios.bygul.model.lighting;

import org.lwjgl.util.vector.Vector3f;

public class SunLight extends DirectionalLight
{
    private float lightAngle = 0;

    public SunLight(Vector3f color, Vector3f direction, float intensity)
    {
        super(color, direction, intensity);
    }

    public SunLight(DirectionalLight light)
    {
        super(light);
    }

    public float getLightAngle()
    {
        return lightAngle;
    }

    public void setLightAngle(float lightAngle)
    {
        this.lightAngle = lightAngle;
    }

    public void update()
    {
        lightAngle += 1.1f;
        if (lightAngle > 90) {
            this.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float)(Math.abs(lightAngle) - 80)/ 10.0f;
            this.setIntensity(factor);
            this.getColor().y = Math.max(factor, 0.9f);
            this.getColor().z = Math.max(factor, 0.5f);
        } else {
            this.setIntensity(1);
            this.getColor().x = 1;
            this.getColor().y = 1;
            this.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        this.getDirection().x = (float) Math.sin(angRad);
        this.getDirection().y = (float) Math.cos(angRad);
    }
}
