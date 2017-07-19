package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.EnumTileShape;
import com.deadvikingstudios.norsetown.model.world.World;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * A basic unit in a voxel world
 */
public abstract class Tile
{
    public static final float TILE_SIZE = 0.5f;
    public static final float TILE_HEIGHT = 0.5f;

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

    /**
     * physically takes up entire tilespace
     * @return
     */
    public boolean isSolid()
    {
        return isSolid && !isAir();
    }

    public Tile setSolid(boolean solid)
    {
        isSolid = solid;
        return this;
    }

    /**
     * Visually takes up entire tilespace
     * @return
     */
    public boolean isOpaque()
    {
        return isOpaque && !isAir();
    }

    public Tile setOpaque(boolean opaque)
    {
        isOpaque = opaque;
        return this;
    }

    public boolean isFullTile()
    {
        return this.isOpaque() && this.isSolid();
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

    public abstract void update(World world, int x, int y, int z);

    public abstract EnumTileShape getRenderType(int metadata);

    public boolean isReplacable()
    {
        return false;
    }

    public static class Tiles
    {
        private static Tile tiles[] = new Tile[256];

        public static Tile tileAir;
        public static Tile tileGrass;
        public static Tile tileSoil;
        public static Tile tileLogThin;
        public static Tile tileLogMed;
        public static Tile tileLogThick;
        public static Tile tileLogFull;
        public static Tile tilePlank;
        public static Tile tileStoneCliff;
        public static Tile tileStoneCobble;
        public static Tile tileClay;
        public static Tile tileWattleDaub;
        public static Tile tileLeaves;
        public static Tile tileGrassTall;

        public static void init()
        {
            int i =0;

            register(tileAir = new TileAir(i++,"tile_air").setOpaque(false).setSolid(false));
            register(tileGrass = new TileSod(i++,"tile_sod", EnumMaterial.EARTH).setTextureOffset(2,2,2,2,1,3));
            register(tileSoil = new TileSoil(i++, "tile_soil", EnumMaterial.EARTH).setTextureOffset(3));
            register(tileLogThin = new TileTree(i++,"tile_log_thin").setOpaque(false).setTextureOffset(6,5));
            register(tileLogMed = new TileTree(i++,"tile_log_med").setOpaque(false).setTextureOffset(6,5));
            register(tileLogThick = new TileTree(i++,"tile_log_thick").setOpaque(false).setTextureOffset(6,5));
            register(tileLogFull = new TileTree(i++,"tile_log_fill").setOpaque(true).setTextureOffset(6,5));
            register(tilePlank = new TileWood(i++,"tile_plank", EnumMaterial.WOOD).setTextureOffset(4));
            register(tileStoneCliff = new TileStone(i++,"tile_stone_cliff", EnumMaterial.STONE).setTextureOffset(7));
            register(tileStoneCobble = new TileStone(i++,"tile_stone_cobble", EnumMaterial.STONE).setTextureOffset(8));
            register(tileClay = new TileSoil(i++,"tile_clay", EnumMaterial.EARTH).setTextureOffset(9));
            register(tileWattleDaub = new TileWood(i++,"tile_wattledaub", EnumMaterial.EARTH).setTextureOffset(9,10));
            register(tileLeaves = new TileLeaves(i++,"tile_leaves", EnumMaterial.PLANT).setOpaque(false).setTextureOffset(16));
            register(tileGrassTall = new Tile(i++, "tile_grass_tall", EnumMaterial.PLANT)
            {
                @Override
                public void update(World world, int x, int y, int z)
                {

                }

                @Override
                public EnumTileShape getRenderType(int metadata)
                {
                    return EnumTileShape.CROSS;
                }

                @Override
                public boolean isReplacable()
                {
                    return true;
                }
            }.setOpaque(false).setTextureOffset(17));
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
        PLANT, //Plants
        ICE, //Snow, Sleet, Ice
        WATER; //Salt Water, Brine, Fresh Water, Waves, Rapids
    }
}


