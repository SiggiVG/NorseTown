package com.deadvikingstudios.norsetown.controller;

import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * Created by SiggiVG on 7/16/2017.
 */
public class MouseInput
{
    public static final int NUM_BUTTONS = Mouse.getButtonCount();

    private static ArrayList<Integer> currentButtons = new ArrayList<Integer>();
    private static ArrayList<Integer> downButtons= new ArrayList<Integer>();
    private static ArrayList<Integer> upButtons = new ArrayList<Integer>();

    public static void update()
    {
        upButtons.clear();
        for (int i = 0; i < NUM_BUTTONS; ++i)
        {
            if (!getButton(i) && currentButtons.contains(i))
            {
                upButtons.add(i);
            }
        }

        downButtons.clear();
        for (int i = 0; i < NUM_BUTTONS; ++i)
        {
            if (getButton(i) && !currentButtons.contains(i))
            {
                downButtons.add(i);
            }
        }

        currentButtons.clear();
        for (int i = 0; i < NUM_BUTTONS; ++i)
        {
            if (getButton(i))
            {
                currentButtons.add(i);
            }
        }

        //System.out.println(Mouse.getButtonName(Mouse.getEventButton()));
    }

    public static boolean getButton(int buttonCode)
    {
        return Mouse.isButtonDown(buttonCode);
    }

    public static boolean getButtonDown(int buttonCode)
    {
        return downButtons.contains(buttonCode);
    }

    public static boolean getButtonUP(int buttonCode)
    {
        return upButtons.contains(buttonCode);
    }


}
