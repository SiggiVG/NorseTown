package com.deadvikingstudios.norsetown.model.world;

public enum EnumDirection
{
    NORTH("north", 0,1),
    NORTH_EAST("north_east",1,1),
    EAST("east",1,0),
    SOUTH_EAST("south_east",1,-1),
    SOUTH("south",0,-1),
    SOUTH_WEST("south_west",-1,-1),
    WEST("west",-1,0),
    NORTH_WEST("north_west",-1,1),
    NULL("",0,0);

    String unlocalizedName;
    int x,z;

    EnumDirection(String unlocalizedName, int x, int z)
    {
        this.unlocalizedName = unlocalizedName;
        this.x = x;
        this.z = z;
    }

    public String getUnlocalizedName()
    {
        return unlocalizedName;
    }

    public int getX()
    {
        return x;
    }

    public int getZ()
    {
        return z;
    }

    public EnumDirection clockwise()
    {
        switch (this)
        {
            case NORTH: return NORTH_EAST;
            case NORTH_EAST: return EAST;
            case EAST: return SOUTH_EAST;
            case SOUTH_EAST: return SOUTH;
            case SOUTH: return SOUTH_WEST;
            case SOUTH_WEST: return WEST;
            case WEST: return NORTH_WEST;
            case NORTH_WEST: return NORTH;
        }
        return NULL;
    }

    public EnumDirection counterClockwise()
    {
        switch (this)
        {
            case NORTH: return NORTH_WEST;
            case NORTH_EAST: return NORTH;
            case EAST: return NORTH_EAST;
            case SOUTH_EAST: return EAST;
            case SOUTH: return SOUTH_EAST;
            case SOUTH_WEST: return SOUTH;
            case WEST: return SOUTH_WEST;
            case NORTH_WEST: return WEST;
        }
        return NULL;
    }

    public EnumDirection getOpposite()
    {
        switch (this)
        {
            case NORTH: return SOUTH;
            case NORTH_EAST: return SOUTH_WEST;
            case EAST: return WEST;
            case SOUTH_EAST: return NORTH_WEST;
            case SOUTH: return NORTH;
            case SOUTH_WEST: return NORTH_EAST;
            case WEST: return EAST;
            case NORTH_WEST: return SOUTH_EAST;
        }
        return NULL;
    }
}
