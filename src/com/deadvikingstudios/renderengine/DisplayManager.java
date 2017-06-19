package com.deadvikingstudios.renderengine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class DisplayManager
{
    /**
     * the Width and Height of the Display
     */
    private static final int WIDTH = 1280, HEIGHT = 720;
    private static final int FPS_CAP = 120;

    private static boolean fullScreen = false;


    /**
     * Creates the Display
     */
    public static void createDisplay()
    {
        //Context Attributes
        ContextAttribs attribs = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        //Creating the Display
        try
        {
            if(!fullScreen)
                Display.setDisplayMode((new DisplayMode(WIDTH, HEIGHT)));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("NorseTown"); //TODO get from a different class
            Display.setFullscreen(fullScreen);
            GL11.glViewport(0,0, Display.getWidth(), Display.getHeight());

        } catch (LWJGLException e)
        {
            e.printStackTrace();
        }
        Mouse.setGrabbed(true);
    }

    /**
     * Updates the Display
     */
    public static void updateDisplay()
    {
        Display.sync(FPS_CAP);
        Display.update();

        while(Keyboard.next())
        {
            if(Keyboard.getEventKeyState())
            {
                if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
                {
                    closeDisplay();
                }

                if(Keyboard.isKeyDown(Keyboard.KEY_E) && Mouse.isGrabbed())
                {
                    Mouse.setGrabbed(false);
                }
                else if(Keyboard.isKeyDown(Keyboard.KEY_E) && !Mouse.isGrabbed())
                {
                    Mouse.setGrabbed(true);
                }
            }
        }
    }

    /**
     * Closs the Display
     */
    public static void closeDisplay()
    {
        Display.destroy();
        System.exit(0);
    }
}
