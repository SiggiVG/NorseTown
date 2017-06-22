package com.deadvikingstudios.norsetown.model.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class Camera extends Entity
{
    private float speed = 0.1f;
    private float turnSpeed = 0.1f;
    private float moveAt = 0;

    public Camera(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ, float scale)
    {
        super(posX, posY, posZ, rotationX, rotationY, rotationZ, scale);
    }

    public Camera(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ)
    {
        super(posX, posY, posZ, rotationX, rotationY, rotationZ);
    }

    public Camera(Vector3f position, float rotationX, float rotationY, float rotationZ, float scale)
    {
        super(position, rotationX, rotationY, rotationZ, scale);
    }

    public Camera(Vector3f position, float rotationX, float rotationY, float rotationZ)
    {
        super(position, rotationX, rotationY, rotationZ);
    }

    public Camera(Vector3f position, Vector3f rotation, float scale)
    {
        super(position, rotation, scale);
    }

    public Camera(Vector3f position, Vector3f rotation)
    {
        super(position, rotation);
    }

    public Camera(Vector3f position, float scale)
    {
        super(position, scale);
    }

    public Camera(Vector3f position)
    {
        super(position);
    }

    public Camera(float x, float y, float z, float scale)
    {
        super(x, y, z, scale);
    }

    public Camera(float x, float y, float z)
    {
        super(x, y, z);
    }

    //TODO move to a CameraController in the Controller Directory
    public void move()
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            moveAt = -speed;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            moveAt = speed;
        }
        else
        {
            moveAt = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            moveAt *= 10f;
        }

        rotationX += -Mouse.getDY() * turnSpeed;
        rotationY += Mouse.getDX() * turnSpeed;

        float dx = -(moveAt * (float) Math.sin(Math.toRadians(rotationY)));
        float dy = (moveAt * (float) Math.sin(Math.toRadians(rotationX)));
        float dz = (moveAt * (float) Math.cos(Math.toRadians(rotationY)));

        posX += dx;
        posY += dy;
        posZ += dz;
    }
}
