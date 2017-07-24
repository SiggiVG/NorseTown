package com.deadvikingstudios.norsetown.controller;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * This is the class that controls the CameraController Viewport and it's movement that results in a transformation of
 * what is rendered to the screen.
 */
public class CameraController
{
    public boolean isOrthogonal = false;

    private Vector3f position;
    /**
     * x = roll
     * y = pitch
     * z = yaw
     */
    private Vector3f rotation;

    private float speed = 0.2f;
    private float turnSpeed = 0.1f;
    public static final float UPPER_LIMIT = 1000f, LOWER_LIMIT = 0f;

    public CameraController(float x, float y, float z)
    {
        position = new Vector3f(x,y,z);
        rotation = new Vector3f(0,0,0);
    }

    public void move()
    {
        if(KeyboardInput.getKeyDown(Keyboard.KEY_TAB))
        {
            isOrthogonal = !isOrthogonal;
            if(isOrthogonal)
            {
                rotation.x = 35;

                if(rotation.y < 90)
                {
                    rotation.y = 45;
                }
                else if(rotation.y < 180)
                {
                    rotation.y = 135;
                }
                else if(rotation.y < 270)
                {
                    rotation.y = 225;
                }
                else if(rotation.y < 360)
                {
                    rotation.y = 315;
                }
                Mouse.setGrabbed(false);
                Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
            }
            else
            {
                Mouse.setGrabbed(true);
            }
        }


        float moveAt = speed;

        if(KeyboardInput.getKey(Keyboard.KEY_LSHIFT))
        {
            moveAt *= 5f;
        }

        if(isOrthogonal)
        {
            if(rotation.y == 45)
            {
                if (KeyboardInput.getKey(Keyboard.KEY_W))
                {
                    position.z -= moveAt;
                    position.x += moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_S))
                {
                    position.z += moveAt;
                    position.x -= moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_A))
                {
                    position.x -= moveAt;
                    position.z -= moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_D))
                {
                    position.x += moveAt;
                    position.z += moveAt;
                }
            }
            else if(rotation.y == 135)
            {
                if (KeyboardInput.getKey(Keyboard.KEY_W))
                {
                    position.x += moveAt;
                    position.z += moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_S))
                {
                    position.x -= moveAt;
                    position.z -= moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_A))
                {
                    position.z -= moveAt;
                    position.x += moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_D))
                {
                    position.z += moveAt;
                    position.x -= moveAt;
                }
            }
            else if(rotation.y == 225)
            {
                if (KeyboardInput.getKey(Keyboard.KEY_W))
                {
                    position.z += moveAt;
                    position.x -= moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_S))
                {
                    position.z -= moveAt;
                    position.x += moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_A))
                {
                    position.x += moveAt;
                    position.z += moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_D))
                {
                    position.x -= moveAt;
                    position.z -= moveAt;
                }
            }
            else if(rotation.y == 315)
            {
                if (KeyboardInput.getKey(Keyboard.KEY_W))
                {
                    position.x -= moveAt;
                    position.z -= moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_S))
                {
                    position.x += moveAt;
                    position.z += moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_A))
                {
                    position.z += moveAt;
                    position.x -= moveAt;
                }
                if (KeyboardInput.getKey(Keyboard.KEY_D))
                {
                    position.z -= moveAt;
                    position.x += moveAt;
                }
            }
        }
        else
        {
            //rotation start
            if(Mouse.isGrabbed())
            {
                roll(-Mouse.getDY() * turnSpeed);
                pitch(Mouse.getDX() * turnSpeed);
            }
            //rotation end

            float dx=0;
            float dy=0;
            float dz=0;

            if (KeyboardInput.getKey(Keyboard.KEY_W))
            {
                dx = (moveAt * (float) Math.sin(Math.toRadians(rotation.y)));
                dy = -(moveAt * (float) Math.sin(Math.toRadians(rotation.x)));
                dz = -(moveAt * (float) Math.cos(Math.toRadians(rotation.y)));
            }
            if (KeyboardInput.getKey(Keyboard.KEY_S))
            {
                dx = -(moveAt * (float) Math.sin(Math.toRadians(rotation.y)));
                dy = (moveAt * (float) Math.sin(Math.toRadians(rotation.x)));
                dz = (moveAt * (float) Math.cos(Math.toRadians(rotation.y)));
            }
            if (KeyboardInput.getKey(Keyboard.KEY_A))
            {
                dx = (moveAt * (float) Math.sin(Math.toRadians(rotation.y-90)));
                //dy = (moveAt * (float) Math.sin(Math.toRadians(rotation.x)));
                dz = -(moveAt * (float) Math.cos(Math.toRadians(rotation.y-90)));
            }
            if (KeyboardInput.getKey(Keyboard.KEY_D))
            {
                dx = -(moveAt * (float) Math.sin(Math.toRadians(rotation.y-90)));
                //dy = (moveAt * (float) Math.sin(Math.toRadians(rotation.x)));
                dz = (moveAt * (float) Math.cos(Math.toRadians(rotation.y-90)));
            }

            translate(dx,dy,dz);
        }


        if (KeyboardInput.getKey(Keyboard.KEY_E))
        {
            position.y += moveAt;
        }
        else if (KeyboardInput.getKey(Keyboard.KEY_Q))
        {
            position.y -= moveAt;
        }

        if(position.y < LOWER_LIMIT)
        {
            position.y = LOWER_LIMIT;
        }
        else if(position.y > UPPER_LIMIT)
        {
            position.y = UPPER_LIMIT;
        }
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(float x, float y, float z)
    {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void translate(float x, float y, float z)
    {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public Vector3f getRotation()
    {
        return rotation;
    }

    public float getRoll()
    {
        return rotation.x;
    }

    public float getPitch()
    {
        return rotation.y;
    }

    public float getYaw()
    {
        return rotation.z;
    }

    public void roll(float degree)
    {
        rotation.x += degree;

        if(rotation.x > 90)
        {
            rotation.x = 90;
        }
        if(rotation.x < -90)
        {
            rotation.x = -90;
        }
    }

    public void pitch(float degree)
    {
        rotation.y += degree;

        if(rotation.y >= 360)
        {
            rotation.y -= 360;
        } else if(rotation.y < 0)
        {
            rotation.y += 360;
        }
    }

    public void yaw(float degree)
    {
        rotation.z += degree;

        if(rotation.z > 90)
        {
            rotation.z = 90;
        }
        if(rotation.z < -90)
        {
            rotation.z = -90;
        }
    }

    public void setRotation(float x, float y, float z)
    {
        rotation = new Vector3f(x, y, z);
    }

    public void setRotation(Vector3f rot)
    {
        rotation = rot;
    }

    public void addRotation(float x, float y, float z)
    {
        roll(x);
        pitch(y);
        yaw(z);
    }
}
