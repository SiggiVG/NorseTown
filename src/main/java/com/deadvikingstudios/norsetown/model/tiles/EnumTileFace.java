package com.deadvikingstudios.norsetown.model;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public enum EnumTileFace
{
    NORTH("north", new Vector3f(0,0,1)),
    EAST("east", new Vector3f(1,0,0)),
    SOUTH("south", new Vector3f(0,0,-1)),
    WEST("west", new Vector3f(-1,0,0)),
    TOP("top", new Vector3f(0,1,0)),
    BOTTOM("bottom", new Vector3f(0,-1,0)),
    PARTICLE("particle", new Vector3f(0,0,0)),
    NULL("null", new Vector3f(0,0,0));

    private String name;
    private Vector3f offset;
    private EnumTileFace[] values;

    EnumTileFace(String name, Vector3f off)
    {
        this.name = name;
        this.offset = off;
    }

    public static EnumTileFace get(int i)
    {
        switch (i)
        {
            case 0: return NORTH;
            case 1: return EAST;
            case 2: return SOUTH;
            case 3: return WEST;
            case 4: return TOP;
            case 5: return BOTTOM;
            case 6: return PARTICLE;
            default: return null;
        }
    }

    public EnumTileFace getOpposite()
    {
        switch (this)
        {
            case NORTH: return SOUTH;
            case EAST: return WEST;
            case SOUTH: return NORTH;
            case WEST: return EAST;
            case TOP: return BOTTOM;
            case BOTTOM: return TOP;
            default:return NULL;

        }
    }
}
