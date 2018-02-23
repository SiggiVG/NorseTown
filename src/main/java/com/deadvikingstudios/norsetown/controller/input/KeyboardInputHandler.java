package com.deadvikingstudios.norsetown.controller.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by SiggiVG on 7/15/2017.
 */
public class KeyboardInputHandler extends GLFWKeyCallback
{
    private long window;
    private static final int NUM_KEYCODES = 65535;
    private static final int NUM_MOUSE_BUTTONS = 8;

    private static int[] keyStates = new int[NUM_KEYCODES];
    private static boolean[] activeKeys = new boolean[NUM_KEYCODES];

    private static int[] mouseButtonStates = new int[NUM_MOUSE_BUTTONS];
    private static boolean[] activeMouseButtons = new boolean[NUM_MOUSE_BUTTONS];
    private static long lastMouseNS = 0;
    private static long mouseDoubleClickPersiodNS = 1_000_000_000 / 5;


    @Override
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        activeKeys[key] = action != GLFW_RELEASE;
        keyStates[key] = action;
    }



    public static boolean isKeyPressed(int keyCode)
    {
        return activeKeys[keyCode];
    }

    public static boolean isKeyDown(int keyCode)
    {
        return isKeyPressed(keyCode);//downKeys.contains(keyCode);
    }
    /*
    public static boolean isKeyReleased(int keyCode)
    {
        return upKeys.contains(keyCode);
    }*/

}
