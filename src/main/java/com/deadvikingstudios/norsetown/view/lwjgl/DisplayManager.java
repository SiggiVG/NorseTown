package com.deadvikingstudios.norsetown.view.lwjgl;

import com.deadvikingstudios.norsetown.controller.GameContainer;
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
    public static final int WIDTH = 1280, HEIGHT = 720;

    private static boolean fullScreen = false;


    /**
     * Creates the Display
     */
    public static void create()
    {
        //Context Attributes
        ContextAttribs attribs = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        //Creating the Display
        try
        {
            Display.setTitle(GameContainer.GAME_NAME + " " + GameContainer.VERSION);
            Display.setResizable(true);
            if(!fullScreen) { Display.setDisplayMode((new DisplayMode(WIDTH, HEIGHT))); }
            Display.setFullscreen(fullScreen);
            Display.setVSyncEnabled(true);
            Display.create(new PixelFormat(), attribs);

            //Display.setFullscreen(fullScreen);
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

            //GL11.glViewport(0,0, Display.getWidth(), Display.getHeight());

        } catch (LWJGLException e)
        {
            e.printStackTrace();
        }
        //binds the mouse
        Mouse.setGrabbed(true);
        Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
    }

    /**
     * Updates the Display
     */
    public static void updateDisplay()
    {
        Display.update();
        Display.sync(GameContainer.TARGET_FPS);

        //Keyboard input for dev
        while(Keyboard.next())
        {
            if(Keyboard.getEventKeyState())
            {
                //ESC = quit
                if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
                {
                    dispose();
                }

                //E = unbind/bind mouse from game
                if(Keyboard.isKeyDown(Keyboard.KEY_R) && Mouse.isGrabbed())
                {
                    Mouse.setGrabbed(false);
                    Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
                }
                else if(Keyboard.isKeyDown(Keyboard.KEY_R) && !Mouse.isGrabbed())
                {
                    Mouse.setGrabbed(true);
                    Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
                }
            }
        }
    }

    public static void resize()
    {
        GL11.glViewport(0,0, Display.getWidth(), Display.getHeight());
        GameContainer.renderer.createProjectionMatrix(GameContainer.shader, false);

    }

    /**
     * Closes the Display
     */
    public static void dispose()
    {
        //empties the vertices
        GameContainer.loader.cleanUp();

        Display.destroy();
        System.exit(0);
    }
}
