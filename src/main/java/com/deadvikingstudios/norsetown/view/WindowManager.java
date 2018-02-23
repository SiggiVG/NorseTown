package com.deadvikingstudios.norsetown.view;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.controller.input.KeyboardInputHandler;
import com.deadvikingstudios.norsetown.controller.input.MouseInputHandler;
import com.deadvikingstudios.norsetown.controller.input.MousePositionHandler;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 * Created by SiggiVG on 6/19/2017.
 */
public class WindowManager
{
    /**
     * the Width and Height of the Display
     */
    private static final int WIDTH = 1280, HEIGHT = 720;

    private static boolean fullScreen = false;

    public static long window;
    private static GLFWKeyCallback keyCallback;
    private static GLFWMouseButtonCallback mouseButtonCallback;
    private static GLFWCursorPosCallback cursorPosCallback;


    /**
     * Creates the Display
     */
    public static void create()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_DEPTH_BITS, 24);

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, GameContainer.GAME_NAME + " " + GameContainer.VERSION, NULL, NULL);
        if ( window == NULL )
        {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            pWidth = stack.mallocInt(1); // int*
            pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        /*
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });*/

        //Input Callbacks
        glfwSetKeyCallback(window, keyCallback = new KeyboardInputHandler());
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new MouseInputHandler());
        glfwSetCursorPosCallback(window, cursorPosCallback = new MousePositionHandler());

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    private static IntBuffer pWidth;
    private static IntBuffer pHeight;

    /**
     * Updates the Display
     */
    public static void updateDisplay()
    {
        if(KeyboardInputHandler.isKeyPressed(GLFW_KEY_ESCAPE))
        {
            GameContainer.game.stop();
            dispose();
        }

        GLFW.glfwSwapBuffers(WindowManager.window);
    }

    public static void resize()
    {
        int w = pWidth.get(0);
        int h = pHeight.get(0);
        if(w != getCurrentWidth() || h != getCurrentHeight())
        {
            GL11.glViewport(0, 0, getCurrentWidth(), getCurrentHeight());
            GameContainer.renderer.createProjectionMatrix(GameContainer.renderer.getShader(), false);
        }
    }

    /**
     * Closes the Display
     */
    public static void dispose()
    {
        //empties the vertices
        GameContainer.loader.cleanUp();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        System.exit(0);
    }

    public static int getCurrentWidth()
    {
        glfwGetWindowSize(window, pWidth, null);
        return pWidth.get(0);
    }

    public static int getCurrentHeight()
    {
        glfwGetWindowSize(window, null, pHeight);
        return pHeight.get(0);
    }

    public static int getHalfWidth()
    {
        return getCurrentWidth()/2;
    }

    public static int getHalfHeight()
    {
        return getCurrentHeight()/2;
    }
}
