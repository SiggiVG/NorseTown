package com.deadvikingstudios.bygul.model.tiles;

import com.deadvikingstudios.bygul.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.view.meshes.TileMesh;
import com.sun.istack.internal.NotNull;

import java.util.Collections;

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

    protected final EnumMaterial MATERIAL;

    /**
     * true if it causes collisions
     */
    protected boolean isSolid = true;
    /**
     * true if it has no transparent pixels
     */
    protected boolean isOpaque = true;

    private int index;

    public int getIndex(){return index;}

    private void setIndex(int index){this.index = index;}

    public Tile(String unlocalizedName, EnumMaterial material)
    {
        this.UNLOCALIZED_NAME = "tile_" + unlocalizedName;
        this.MATERIAL = material;
    }

    public String getUnlocalizedName()
    {
        return UNLOCALIZED_NAME;
    }


    /**
     * @param metadata You have 128 values to play with, have fun
     * Note that each TileMesh object contains all the data for the 2^6 possible cullface norsetown.meshes generated from
     * it's cuboid data. These norsetown.meshes have their vertices offset at render time when constructing the ChunkColumn Mesh
     * TODO: Load data from file
     */
    public abstract TileMesh getTileMesh(int metadata, int x, int y, int z);
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
        else return false;
    }

    @Override
    public String toString()
    {
        return "Tile{" +
                "unlocalizedName='" + UNLOCALIZED_NAME + '\'' +
                '}';
    }

    @Override
    public int hashCode()
    {
        return UNLOCALIZED_NAME.hashCode();
    }

    //**** EVENTS ****//
    public boolean onTilePlaced(int x, int y, int z, int metadata, boolean byPlayer)
    {
        //do nothing
        return true;
    }

    public boolean onTileDestroyed(int x, int y, int z, int metadata, boolean byPlayer)
    {
        //do nothing
        return true;
    }

    public static class Tiles
    {
        private static Tile[] tiles = new Tile[512];

        public static int getNextTileIndex()
        {
            for (int i = 1; i < tiles.length; i++)
            {
                if(tiles[i] == null)
                    return i;
            }
            //throw an error
            return -1;
        }

        //is final to ensure that tileAir is always an instance of TileAir
        public static final Tile tileAir = new TileAir("air").setOpaque(false);
        static
        {
            register(0, tileAir);
        }


        /**
         * Use this method to register an instance of a Tile.
         * @param tile
         *
         * @throws IllegalArgumentException Only {@link Tile.Tiles#tileAir} is permitted to be registered to index 0
         */
        public static void register(int index, @NotNull Tile tile)
        {
            if(index == 0 && tile != tileAir)
            {
                throw new IllegalArgumentException("Not permitted to register a Tile that is not tileAir to index 0");
            }
            if(tile == null) throw new IllegalArgumentException("Cannot register a null tile");
            tile.setIndex(index);
            tiles[index] = tile;

        }

        public static void register(@NotNull Tile tile)
        {
            register(getNextTileIndex(), tile);
        }

//        public static void register( Class<? extends Tile> tile, int index, String unlocalizedName, EnumMaterial material)
//        {
//            Tile tile =
//        }

        /**
         * Used to unregister instances of NorseTiles.
         * @throws IllegalArgumentException {@link Tile.Tiles#tileAir}  and index 0 are not permitted to be unregistered
         */
        public static void unregister(int index, Tile tile)
        {
            if(index == 0 || tile == tileAir)
            {
                throw new IllegalArgumentException("Not permitted to unregister tileAir from index 0");
            }
            if(tiles[index] == tile) tiles[index] = null;
        }

        public static Tile get(int id)
        {
            Tile tile = tiles[id];
            return tile == null ? tileAir : tile;
        }

        public static Tile get(String unlocalizedName)
        {
            int i = 0;
            while(tiles[i] != null)
            {
                if(tiles[i].getUnlocalizedName().equals("tile_" + unlocalizedName))
                {
                    return tiles[i];
                }
            }
            return null;
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


