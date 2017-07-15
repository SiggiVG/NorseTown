package com.deadvikingstudios.norsetown.model.tiles;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * A basic unit in a voxel world
 */
public class Tile
{
    public static final float TILE_SIZE = 1f;
    public static final float TILE_HEIGHT = 0.5f;

    public static final int TERRAIN_TEXTURE_ROWS = 16;
    public static final int TERRAIN_TEXTURE_COLS = 16;

    /**
     * unlocalized name, used for localization and texturing
     */
    protected final String UNLOCALIZED_NAME;
    /**
     * the index it is stored at in the Tiles array
     */
    protected final byte INDEX;

    protected final EnumMaterial MATERIAL;

    /**
     * true if it causes collisions
     */
    protected boolean isSolid = true;
    /**
     * true if it has no transparent pixels
     */
    protected boolean isOpaque = true;



    public Tile(int index, String unlocalizedName, EnumMaterial material)
    {
        this.INDEX = (byte)index;
        this.UNLOCALIZED_NAME = unlocalizedName;
        this.MATERIAL = material;
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

    private int[] textureOffsets = new int[6];

    /**
     * @param side
     * @return the UV offset
     */
    public int getTextureOffset(int side)
    {
        return textureOffsets[side];
    }

    public Tile setTextureOffset(int allSides)
    {
        for (int i = 0; i < textureOffsets.length; i++)
        {
            textureOffsets[i] = allSides;
        }
        return this;
    }

    public Tile setTextureOffset(int columnEnds, int columnSides)
    {
        textureOffsets[4] = textureOffsets[5] = columnEnds;
        textureOffsets[0] = textureOffsets[1] = textureOffsets[2] = textureOffsets[3] = columnSides;
        return this;
    }

    public Tile setTextureOffset(int north, int east, int south, int west, int top, int bottom)
    {
        textureOffsets[0] = north;
        textureOffsets[1] = east;
        textureOffsets[2] = south;
        textureOffsets[3] = west;
        textureOffsets[4] = top;
        textureOffsets[5] = bottom;
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
        public static Tile tileSoil;

        public static void init()
        {
            register(tileAir = new Tile(0,"tile_air", EnumMaterial.AIR).setOpaque(false).setSolid(false));
            register(tileGrass = new Tile(1,"tile_sod", EnumMaterial.EARTH).setTextureOffset(2,2,2,2,1,3));
            register(tileSoil = new Tile(2, "tile_soil", EnumMaterial.EARTH).setTextureOffset(3));
        }

        public static void register(Tile tile)
        {
            tiles[tile.getIndex()] = tile;
        }

        public static void unregister(Tile tile)
        {
            tiles[tile.getIndex()] = null;
        }

        public static Tile get(int id)
        {
            Tile tile = tiles[id];
            return tile == null ? tileAir : tile;
        }
    }

    /**
     * TODO have return a tooltype
     * @return
     */
    public int getToolRequired()
    {
        return 0;
    }

    public enum EnumMaterial
    {
        AIR, //Air, Cloud, Wind
        EARTH, //Soil, Clay, Wattle & Daub, Sod
        STONE, //Rocks, Stone, Mountains
        METAL, //Copper, Iron, Gold, Silver
        WOOD, //Logs, Planks, Firewood
        ICE, //Snow, Sleet, Ice
        WATER; //Salt Water, Brine, Fresh Water, Waves, Rapids
    }
}


