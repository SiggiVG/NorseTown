package com.deadvikingstudios.bygul.controller.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by SiggiVG on 7/15/2017.
 */
public class KeyboardInputHandler extends GLFWKeyCallback
{
    private static final int NUM_KEYCODES = 65535;

    private static int[] keyStates = new int[NUM_KEYCODES];
    private static boolean[] activeKeys = new boolean[NUM_KEYCODES];

    private static final int NO_STATE = -1;

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        activeKeys[key] = action != GLFW_RELEASE;
        keyStates[key] = action;
    }

    public static void update()
    {
        for (int i = 0; i < keyStates.length; i++)
        {
            keyStates[i] = NO_STATE;
        }
    }

    public static boolean isKeyPressed(int keyCode)
    {
        return keyStates[keyCode] == GLFW_PRESS;
    }

    public static boolean isKeyDown(int keyCode)
    {
        return activeKeys[keyCode];
    }

    public static boolean isKeyReleased(int keyCode)
    {
        return keyStates[keyCode] == GLFW_RELEASE;
    }

}
