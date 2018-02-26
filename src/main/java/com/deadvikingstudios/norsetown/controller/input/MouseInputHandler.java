package com.deadvikingstudios.norsetown.controller.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInputHandler extends GLFWMouseButtonCallback
{
    public static final int NUM_BUTTONS = 8;

    private static int[] mouseButtonStates = new int[NUM_BUTTONS];
    private static boolean[] activeMouseButtons = new boolean[NUM_BUTTONS];

    private static final int NO_STATE = -1;

    @Override
    public void invoke(long window, int button, int action, int mods)
    {
        activeMouseButtons[button] = action != GLFW_RELEASE;
        mouseButtonStates[button] = action;
    }

    public static void update()
    {
        for (int i = 0; i < mouseButtonStates.length; i++)
        {
            mouseButtonStates[i] = NO_STATE;
        }

//        long now = System.nanoTime();

//        if (now - lastMouseNS > mouseDoubleClickPeriodNS)
//            lastMouseNS = 0;
    }



    public static boolean isButtonPressed(int buttonCode)
    {
        return mouseButtonStates[buttonCode] == GLFW_PRESS;
    }

    public static boolean isButtonDown(int buttonCode)
    {
        return activeMouseButtons[buttonCode];
    }

    public static boolean isButtonReleased(int buttonCode)
    {
        return mouseButtonStates[buttonCode] == GLFW_RELEASE;
    }
}
