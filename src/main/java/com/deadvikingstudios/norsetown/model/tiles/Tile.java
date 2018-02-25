package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.RegistrationException;
import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * A basic unit in a voxel world
 */
public abstract class Tile
{
    public static final float TILE_SIZE = 1f; //setting to values other than 1 currently break rendering
    public static final float TILE_HEIGHT = 1f;

    private AxisAlignedBoundingBox aabb = new AxisAlignedBoundingBox(0,0,0,1,1,1);

    /**
     * unlocalized name, used for localization and texturing
     */
    protected final String UNLOCALIZED_NAME;
    /**
     * the index it is stored at in the Tiles array
     */
    protected final int INDEX;

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
        this.INDEX = index;
        this.UNLOCALIZED_NAME = "tile_" + unlocalizedName;
        this.MATERIAL = material;

        if(this.INDEX < 0) try
        {
            throw new RegistrationException("Tile: " + UNLOCALIZED_NAME + " was initialized with a negative index");
        } catch (RegistrationException e)
        {
            e.printStackTrace();
        }
    }

    public String getUnlocalizedName()
    {
        return UNLOCALIZED_NAME;
    }

    public int getIndex()
    {
        return INDEX;
    }

    public abstract TileMesh getTileMesh(int metadata);
//    public TileMesh getTileMesh(int metadata)
//    {
//        return null;
//    }

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

    public boolean isFullCuboid(){return true;}

    public boolean isSolidCuboid(){return this.isOpaque && isFullCuboid();}

    public boolean isSideSolid(EnumTileFace side)
    {
        return true;
    }

    /**
     * @return flags it to not render
     */
    public boolean isAir()
    {
        return this == Tiles.tileAir;// || (!isSolid && !isOpaque);
    }

    public abstract void update(Structure structure, int x, int y, int z);

    public boolean isReplacable()
    {
        return false;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        if (INDEX != tile.INDEX) return false;
        return UNLOCALIZED_NAME != null ? UNLOCALIZED_NAME.equals(tile.UNLOCALIZED_NAME) : tile.UNLOCALIZED_NAME == null;
    }

    @Override
    public String toString()
    {
        return "Tile{" +
                "unlocalizedName='" + UNLOCALIZED_NAME + '\'' +
                ",index=" + INDEX +
                '}';
    }

    @Override
    public int hashCode()
    {
        int result = UNLOCALIZED_NAME != null ? UNLOCALIZED_NAME.hashCode() : 0;
        result = 31 * result + INDEX;
        return result;
    }

    public static class Tiles
    {
        private static Tile[] tiles = new Tile[512];

        public static Tile tileAir;
        public static Tile tileGrass;
        public static Tile tileSoil;
        public static Tile tileTreeBase;
        public static Tile tileTrunkFir;
        public static Tile tilePlank;
        public static Tile tileStoneCliff;
        public static Tile tileStoneCobble;
        public static Tile tileClay;
        public static Tile tileWattleDaub;
        public static Tile tileLeaves;
        public static Tile tileGrassTall;

        //crops
        public static Tile tileCropOnion;

        public static void init()
        {
            int i =0;

            register(tileAir = new TileAir(i++,"air").setOpaque(false));
            register(tileGrass = new TileSod(i++,"sod", EnumMaterial.EARTH));
            register(tileSoil = new TileSoil(i++, "soil", EnumMaterial.EARTH));
            register(tileTreeBase = new TileLog(i++,"tree_base"));
            register(tileTrunkFir = new TileLog(i++,"log"));
            register(tilePlank = new TileWood(i++,"plank", EnumMaterial.WOOD));
            register(tileStoneCliff = new TileStone(i++,"stone_cliff", EnumMaterial.STONE));
            register(tileStoneCobble = new TileStone(i++,"stone_cobble", EnumMaterial.STONE));
            register(tileClay = new TileSoil(i++,"clay", EnumMaterial.EARTH));
            register(tileWattleDaub = new TileWood(i++,"wattledaub", EnumMaterial.EARTH));
            register(tileLeaves = new TileLeaves(i++,"leaves", EnumMaterial.PLANT).setOpaque(false));
            register(tileGrassTall = new Tile(i++, "grass_tall", EnumMaterial.PLANT)
            {
                @Override
                public TileMesh getTileMesh(int metadata)
                {
                    return null;
                }

                @Override
                public void update(Structure structure, int x, int y, int z)
                {

                }

                @Override
                public boolean isReplacable()
                {
                    return true;
                }
            }.setOpaque(false));
            register(tileCropOnion = new TileCrop(i++, "tile_vegi_onion"));
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

    public AxisAlignedBoundingBox getAABB(int x, int y, int z)
    {
        return aabb;
//        return new AxisAlignedBoundingBox(aabb.getMinX()+x, aabb.getMinY()+y, aabb.getMinZ()+z,
//                aabb.getMaxX()+x, aabb.getMaxY()+y, aabb.getMaxZ()+z);
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
        PLANT, //Vegitation
        ICE, //Snow, Sleet, Ice
        WATER; //Salt Water, Brine, Fresh Water, Waves, Rapids
    }
}


