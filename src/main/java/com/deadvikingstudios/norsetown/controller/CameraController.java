package com.deadvikingstudios.norsetown.controller;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * This is the class that controls the CameraController Viewport and it's movement that results in a transformation of
 * what is rendered to the screen.
 */
public class CameraController
{
    public static boolean isOrthogonal = false;

    private static Vector3f position = new Vector3f(0,0,0);
    /**
     * x = roll
     * y = pitch
     * z = yaw
     */
    private static Vector3f rotation = new Vector3f(0,0,0);;

    private static float speed = 0.2f;
    private static float turnSpeed = 0.1f;

    public static void move()
    {
        if(InputKeyboard.getKeyDown(Keyboard.KEY_TAB))
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
            }
        }


        float moveAt = speed;

        if(InputKeyboard.getKey(Keyboard.KEY_LSHIFT))
        {
            moveAt *= 5f;
        }

        if(isOrthogonal)
        {
            if(rotation.y == 45)
            {
                if (InputKeyboard.getKey(Keyboard.KEY_W))
                {
                    position.z -= moveAt;
                    position.x += moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_S))
                {
                    position.z += moveAt;
                    position.x -= moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_A))
                {
                    position.x -= moveAt;
                    position.z -= moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_D))
                {
                    position.x += moveAt;
                    position.z += moveAt;
                }
            }
            else if(rotation.y == 135)
            {
                if (InputKeyboard.getKey(Keyboard.KEY_W))
                {
                    position.x += moveAt;
                    position.z += moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_S))
                {
                    position.x -= moveAt;
                    position.z -= moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_A))
                {
                    position.z -= moveAt;
                    position.x += moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_D))
                {
                    position.z += moveAt;
                    position.x -= moveAt;
                }
            }
            else if(rotation.y == 225)
            {
                if (InputKeyboard.getKey(Keyboard.KEY_W))
                {
                    position.z += moveAt;
                    position.x -= moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_S))
                {
                    position.z -= moveAt;
                    position.x += moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_A))
                {
                    position.x += moveAt;
                    position.z += moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_D))
                {
                    position.x -= moveAt;
                    position.z -= moveAt;
                }
            }
            else if(rotation.y == 315)
            {
                if (InputKeyboard.getKey(Keyboard.KEY_W))
                {
                    position.x -= moveAt;
                    position.z -= moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_S))
                {
                    position.x += moveAt;
                    position.z += moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_A))
                {
                    position.z += moveAt;
                    position.x -= moveAt;
                }
                if (InputKeyboard.getKey(Keyboard.KEY_D))
                {
                    position.z -= moveAt;
                    position.x += moveAt;
                }
            }
        }
        else
        {
            //rotation start
            roll(-Mouse.getDY() * turnSpeed);
            pitch(Mouse.getDX() * turnSpeed);
            //rotation end

            float dx=0;
            float dy=0;
            float dz=0;

            if (InputKeyboard.getKey(Keyboard.KEY_W))
            {
                dx = (moveAt * (float) Math.sin(Math.toRadians(rotation.y)));
                dy = -(moveAt * (float) Math.sin(Math.toRadians(rotation.x)));
                dz = -(moveAt * (float) Math.cos(Math.toRadians(rotation.y)));
            }
            if (InputKeyboard.getKey(Keyboard.KEY_S))
            {
                dx = -(moveAt * (float) Math.sin(Math.toRadians(rotation.y)));
                dy = (moveAt * (float) Math.sin(Math.toRadians(rotation.x)));
                dz = (moveAt * (float) Math.cos(Math.toRadians(rotation.y)));
            }
            if (InputKeyboard.getKey(Keyboard.KEY_A))
            {
                dx = (moveAt * (float) Math.sin(Math.toRadians(rotation.y-90)));
                //dy = (moveAt * (float) Math.sin(Math.toRadians(rotation.x)));
                dz = -(moveAt * (float) Math.cos(Math.toRadians(rotation.y-90)));
            }
            if (InputKeyboard.getKey(Keyboard.KEY_D))
            {
                dx = -(moveAt * (float) Math.sin(Math.toRadians(rotation.y-90)));
                //dy = (moveAt * (float) Math.sin(Math.toRadians(rotation.x)));
                dz = (moveAt * (float) Math.cos(Math.toRadians(rotation.y-90)));
            }

            translate(dx,dy,dz);
        }


        if (InputKeyboard.getKey(Keyboard.KEY_E))
        {
            position.y += moveAt;
        }
        else if (InputKeyboard.getKey(Keyboard.KEY_Q))
        {
            position.y -= moveAt;
        }
    }

    public static Vector3f getPosition()
    {
        return position;
    }

    public static void setPosition(float x, float y, float z)
    {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public static void translate(float x, float y, float z)
    {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public static Vector3f getRotation()
    {
        return rotation;
    }

    public static float getRoll()
    {
        return rotation.x;
    }

    public static float getPitch()
    {
        return rotation.y;
    }

    public static float getYaw()
    {
        return rotation.z;
    }

    public static void roll(float degree)
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

    public static void pitch(float degree)
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

    public static void yaw(float degree)
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

    public static void setRotation(float x, float y, float z)
    {
        rotation = new Vector3f(x, y, z);
    }

    public static void setRotation(Vector3f rot)
    {
        rotation = rot;
    }

    public static void addRotation(float x, float y, float z)
    {
        roll(x);
        pitch(y);
        yaw(z);
    }
}
