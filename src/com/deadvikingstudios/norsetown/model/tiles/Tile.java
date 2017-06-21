package com.deadvikingstudios.norsetown.model.tiles;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * A basic unit in a voxel world
 */
public class Tile
{
    /**
     * unlocalized name, used for localization and texturing
     */
    protected final String UNLOCALIZED_NAME;
    /**
     * the index it is stored at in the Tiles array
     */
    protected final byte INDEX;

    /**
     * true if it causes collisions
     */
    protected boolean isSolid = true;
    /**
     * true if it has no transparent pixels
     */
    protected boolean isOpaque = true;

    public Tile(int index, String unlocalizedName)
    {
        this.INDEX = (byte)index;
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
        return isSolid && !isAir();
    }

    public Tile setSolid(boolean solid)
    {
        isSolid = solid;
        return this;
    }

    public boolean isOpaque()
    {
        return isOpaque && !isAir();
    }

    public Tile setOpaque(boolean opaque)
    {
        isOpaque = opaque;
        return this;
    }

    /**
     * @return flags it to not render
     */
    public boolean isAir()
    {
        return this == Tiles.tileAir || (!isSolid && !isOpaque);
    }

    public static class Tiles
    {
        private static Tile tiles[] = new Tile[256];

        public static Tile tileAir;
        public static Tile tileGrass;

        public static void init()
        {
            register(tileAir = new Tile(0,"tile_air").setOpaque(false).setSolid(false));
            register(tileGrass = new Tile(1,"tile_grass"));
        }

        public static void register(Tile tile)
        {
            tiles[tile.getIndex()] = tile;
        }

        public static void unregister(Tile tile)
        {
            tiles[tile.getIndex()] = null;
        }

    }
}


