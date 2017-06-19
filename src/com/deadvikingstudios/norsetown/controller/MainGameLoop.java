package com.deadvikingstudios.norsetown.controller;

import com.deadvikingstudios.renderengine.DisplayManager;
import com.deadvikingstudios.renderengine.MasterRenderer;
import org.lwjgl.opengl.Display;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class MainGameLoop
{
    public static void main(String[] args)
    {
        DisplayManager.createDisplay();

        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested())
        {
            renderer.prepare();

            DisplayManager.updateDisplay();
        }

        DisplayManager.closeDisplay();
    }
}
