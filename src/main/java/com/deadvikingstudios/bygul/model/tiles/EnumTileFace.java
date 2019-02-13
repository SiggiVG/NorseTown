package com.deadvikingstudios.bygul.model.tiles;

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

    EnumTileFace(final String name, final Vector3f off)
    {
        this.name = name;
        this.offset = off;
    }

    public static EnumTileFace get(int ordinal)
    {
        return values()[ordinal];
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
            default: return NULL;

        }
    }

    public Vector3f getOffset(int x, int y, int z)
    {
        return new Vector3f(x+offset.x,y+offset.y,z+offset.z);
    }

    public Vector3f getOffset(Vector3f pos)
    {
        return new Vector3f(pos).translate(offset.x, offset.y, offset.z);
    }
}
