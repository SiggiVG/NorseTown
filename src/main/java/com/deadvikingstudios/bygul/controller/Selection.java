package com.deadvikingstudios.bygul.controller;

import com.deadvikingstudios.bygul.controller.input.MouseInputHandler;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 7/16/2017.
 */
public class Selection
{
    private Vector3f corner1 = new Vector3f();;
    private Vector3f corner2 = new Vector3f();

    public void update()
    {
        if(MouseInputHandler.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))
        {
            //Draw a ray originating at the point on the viewport the mouse is at at the angle that the camera is
            //looking.

            //corner1 = corner2
        }
    }
}
