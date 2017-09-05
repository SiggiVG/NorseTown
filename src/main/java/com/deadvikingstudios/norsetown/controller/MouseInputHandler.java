package com.deadvikingstudios.norsetown.controller;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInputHandler extends GLFWMouseButtonCallback
{
    public static final int NUM_BUTTONS = 9;

    private static ArrayList<Integer> currentButtons = new ArrayList<Integer>();
    private static ArrayList<Integer> downButtons= new ArrayList<Integer>();
    private static ArrayList<Integer> upButtons = new ArrayList<Integer>();


    @Override
    public void invoke(long window, int button, int action, int mods)
    {
        if(action != GLFW_RELEASE)
        {
            currentButtons.add(button);
        }
    }

    public static void update()
    {
        upButtons.clear();
        for (int i = 0; i < NUM_BUTTONS; ++i)
        {
            if (!isButtonPressed(i) && currentButtons.contains(i))
            {
                upButtons.add(i);
            }
        }

        downButtons.clear();
        for (int i = 0; i < NUM_BUTTONS; ++i)
        {
            if (isButtonPressed(i) && !currentButtons.contains(i))
            {
                downButtons.add(i);
            }
        }

        currentButtons.clear();
        for (int i = 0; i < NUM_BUTTONS; ++i)
        {
            if (isButtonPressed(i))
            {
                currentButtons.add(i);
            }
        }
    }

    public static boolean isButtonPressed(int buttonCode)
    {
        return currentButtons.contains(buttonCode);
    }

    public static boolean isButtonDown(int buttonCode)
    {
        return downButtons.contains(buttonCode);
    }

    public static boolean isButtonReleased(int buttonCode)
    {
        return upButtons.contains(buttonCode);
    }
}
