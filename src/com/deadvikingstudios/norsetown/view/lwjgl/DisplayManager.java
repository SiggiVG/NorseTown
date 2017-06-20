package com.deadvikingstudios.norsetown.view.lwjgl;

import com.deadvikingstudios.norsetown.controller.MainGameLoop;
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
            //fullscreen control
            if(!fullScreen) { Display.setDisplayMode((new DisplayMode(WIDTH, HEIGHT))); }
            Display.create(new PixelFormat(), attribs);
            Display.setTitle(MainGameLoop.GAME_NAME + " " + MainGameLoop.VERSION);
            Display.setFullscreen(fullScreen);
            //sets the Icon
            /*Display.setIcon(new ByteBuffer[]
                    {
                            new ImageIOImageData().imageToByteBuffer(ImageIO.read(
                                    new File("res/textures/skull_logo.png")),
                                    false, false, null),
                            new ImageIOImageData().imageToByteBuffer(ImageIO.read(
                                    new File("res/textures/skull_logo.png")),
                                    false, false, null)
                    });*/

            GL11.glViewport(0,0, Display.getWidth(), Display.getHeight());

        } catch (LWJGLException e)
        {
            e.printStackTrace();
        }
        //binds the mouse
        Mouse.setGrabbed(true);
    }

    /**
     * Updates the Display
     */
    public static void updateDisplay()
    {
        Display.sync(FPS_CAP);
        Display.update();

        //Keyboard input for dev
        while(Keyboard.next())
        {
            if(Keyboard.getEventKeyState())
            {
                //ESC = quit
                if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
                {
                    closeDisplay();
                }

                //E = unbind/bind mouse from game
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
     * Closes the Display
     */
    public static void closeDisplay()
    {
        //empties the vertices
        MainGameLoop.loader.cleanUp();

        Display.destroy();
        System.exit(0);
    }
}
