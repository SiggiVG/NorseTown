package com.deadvikingstudios.norsetown.model;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public enum EnumFace
{
    North("north", new Vector3f(0,0,1)),
    East("east", new Vector3f(1,0,0)),
    South("south", new Vector3f(0,0,-1)),
    West("west", new Vector3f(-1,0,0)),
    Top("top", new Vector3f(0,1,0)),
    Bottom("bottom", new Vector3f(0,-1,0));

    private String name;
    private Vector3f offset;

    EnumFace(String name, Vector3f off)
    {
        this.name = name;
        this.offset = off;
    }
}
