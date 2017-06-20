package com.deadvikingstudios.norsetown.model.tiles;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class Tile
{
    protected final String UNLOCALIZED_NAME;
    protected final int INDEX;

    protected boolean isSolid = true;
    protected boolean isOpaque = true;

    public Tile(int index, String unlocalizedName)
    {
        this.INDEX = index;
        this.UNLOCALIZED_NAME = unlocalizedName;
    }

    public String getUnlocalizedName()
    {
        return UNLOCALIZED_NAME;
    }

    public int getIndex()
    {
        return INDEX;
    }

    public boolean isSolid()
    {
        return isSolid;
    }

    public void setSolid(boolean solid)
    {
        isSolid = solid;
    }

    public boolean isOpaque()
    {
        return isOpaque;
    }

    public void setOpaque(boolean opaque)
    {
        isOpaque = opaque;
    }

    public boolean isAir()
    {
        return (!isSolid && !isOpaque);
    }

    public class Tiles
    {

    }
}


