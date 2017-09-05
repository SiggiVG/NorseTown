package com.deadvikingstudios.norsetown.controller;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.util.vector.Vector2f;

public class MousePositionHandler extends GLFWCursorPosCallback
{
    private static float xPos, yPos;
    private static float xLast, yLast;
    private static float deltaX, deltaY;

    @Override
    public void invoke(long window, double xpos, double ypos)
    {
        xPos = (float)xpos;
        yPos = (float)ypos;
    }

    public static void update()
    {
        deltaX = xPos - xLast;
        deltaY = yPos - yLast;
        xLast = xPos;
        yLast = yPos;
    }

    public static float getX()
    {
        return xPos;
    }

    public static float getY()
    {
        return yPos;
    }

    public static Vector2f getPosition()
    {
        return new Vector2f(xPos, yPos);
    }

    public static float getDX()
    {
        return deltaX;
    }

    public static float getDY()
    {
        return deltaY;
    }

    public static Vector2f getDeltaPosition()
    {
        return new Vector2f(deltaX, deltaY);
    }
}
