package com.deadvikingstudios.norsetown.controller;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import java.awt.event.MouseListener;

/**
 * Created by SiggiVG on 7/16/2017.
 */
public class Selection
{
    private Vector3f corner1 = new Vector3f();;
    private Vector3f corner2 = new Vector3f();

    public void update()
    {
        if(MouseInput.getButtonDown(0))
        {
            //Draw a ray originating at the point on the viewport the mouse is at at the angle that the camera is
            //looking.

            //corner1 = corner2
        }
    }
}
