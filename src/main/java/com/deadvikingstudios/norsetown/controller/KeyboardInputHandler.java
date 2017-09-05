package com.deadvikingstudios.norsetown.controller;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

/**
 * Created by SiggiVG on 7/15/2017.
 */
public class KeyboardInputHandler extends GLFWKeyCallback
{
    public static final int NUM_KEYCODES = 65535;

    private static boolean[] currentKeys = new boolean[NUM_KEYCODES]; //new ArrayList<Integer>();
    //private static ArrayList<Integer> downKeys = new ArrayList<Integer>();
    //private static ArrayList<Integer> upKeys = new ArrayList<Integer>();

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        currentKeys[key] = action != GLFW_RELEASE;
        /*if(action != GLFW_RELEASE)
        {
            currentKeys.add(key);
        }*/
    }


    public static void update()
    {
        /*upKeys.clear();
        for(int i = 0; i < NUM_KEYCODES; ++i)
        {
            if(!isKeyPressed(i) && currentKeys.contains(i))
            {
                upKeys.add(i);
            }
        }

        downKeys.clear();
        for(int i = 0; i < NUM_KEYCODES; ++i)
        {
            if(isKeyPressed(i) && !currentKeys.contains(i))
            {
                downKeys.add(i);
            }
        }*/

        /*currentKeys.clear();
        for(int i = 0; i < NUM_KEYCODES; ++i)
        {
            if(isKeyPressed(i))
            {
                currentKeys.add(i);
            }
        }*/
    }

    public static boolean isKeyPressed(int keyCode)
    {
        return currentKeys[keyCode];
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
