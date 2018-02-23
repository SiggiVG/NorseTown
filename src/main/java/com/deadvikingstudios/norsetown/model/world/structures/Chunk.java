package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.io.Serializable;
import java.util.Arrays;

public class Chunk implements Serializable
{
    /**
     * @ The position of the chunk
     */
    public final Vector3i position;

    public final static byte SIZE = 16;
    public final static int SIZE_CUBED = SIZE * SIZE * SIZE;

    private final static int TILE_MASK = 0x00_ff_ff_ff;
    private final static int META_MASK = 0xff_00_00_00;

    /**
     * {@link Chunk#TILE_MASK} bits represents the Tile's Id, of which there are 16,777,216 possible values (0-16,777,215)
     * {@link Chunk#META_MASK} bits represents the Tile's Metadata, of which there are 256 possible values (0-256)
     */
    private int[] tiles;
//    private byte[] metadata;

    Chunk(Chunk chunk)
    {
        this.position = new Vector3i(chunk.position);
        this.tiles = Arrays.copyOf(chunk.tiles, SIZE_CUBED);
    }

    /**
     * Generates a new, empty chunk
     * @param position the position within the Structure the chunk is at.
     */
    Chunk(Vector3i position)
    {
        this.position = new Vector3i(position);
        this.tiles = new int[SIZE_CUBED];
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return The position of the structure offset by the position of the chunk
     */
    public Vector3f getRenderPosition(float x, float y, float z)
    {
        return new Vector3f(this.position.x * SIZE + x,
                this.position.y * SIZE + y,
                this.position.z * SIZE + z);
    }

    /**
     *
     * @param structurePos the origin position of the Structure
     * @return The position of the structure offset by the position of the chunk
     */
    public Vector3f getRenderPosition(Vector3f structurePos)
    {
        return this.getRenderPosition(structurePos.x, structurePos.y, structurePos.z);
    }

    /**
     * private method to convert from x,y,z coordinates to a unidimensional array index [0,{@link Chunk#SIZE_CUBED}]
     * @param x    x coord
     * @param y    y coord
     * @param z    z coord
     * @return the array index if x,y,z are all >=0 and < {@link Chunk#SIZE}; else returns -1;
     */
    private int getCoord(int x, int y, int z)
    {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && z >= 0 && z < SIZE)
        {
            return x + (y * SIZE) + (z*SIZE*SIZE);
        }
        return -1;
    }

    /**
     * @param x    x coord, [0,{@link Chunk#SIZE}]
     * @param y    y coord, [0,{@link Chunk#SIZE}]
     * @param z    z coord, [0,{@link Chunk#SIZE}]
     * @return the tile at the location
     * Package private
     */
    Tile getTile(int x, int y, int z)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
            return Tile.Tiles.get(tiles[coord] & TILE_MASK);
        return Tile.Tiles.tileAir;
    }

    /**
     * Sets the tile at the coordinate to the one specified, and the metadata to 0
     * @param tile The tile being set
     * @param x    x coord, [0,{@link Chunk#SIZE}]
     * @param y    y coord, [0,{@link Chunk#SIZE}]
     * @param z    y coord, [0,{@link Chunk#SIZE}]
     * Package private
     */
    void setTile(Tile tile, int x, int y, int z)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
        {
            //Moved same tile check to ChunkColumn
            this.tiles[coord] = tile.getIndex() & TILE_MASK;
        }
    }

    /**
     * Sets the coordinate to the specified tile with the specified metadata
     * @param tile The tile being set
     * @param x    x coord, [0,{@link Chunk#SIZE}]
     * @param y    y coord, [0,{@link Chunk#SIZE}]
     * @param z    y coord, [0,{@link Chunk#SIZE}]
     * @param metaData the metadata value being set, [0,255]
     * Package private
     */
    void setTile(Tile tile, int x, int y, int z, byte metaData)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
        {
            //Moved same tile check to ChunkColumn
            this.tiles[coord] = (tile.getIndex() & TILE_MASK) | (((metaData) << (8*3)) & META_MASK);
        }
    }

    /**
     * Get the metadata at coordinate
     * @param x    x coord, [0,{@link Chunk#SIZE}]
     * @param y    y coord, [0,{@link Chunk#SIZE}]
     * @param z    y coord, [0,{@link Chunk#SIZE}]
     * @return the metadata value of the coordinate, [0,127]
     * Package private
     */
    int getMetadata(int x, int y, int z)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
        {
            //I shouldn't have to mask this
            return (this.tiles[coord] >>> (8*3));
        }
        return 0;
    }

    /**
     * Sets the metadata at the coordinate.
     * @param x    x coord, [0,{@link Chunk#SIZE}]
     * @param y    y coord, [0,{@link Chunk#SIZE}]
     * @param z    y coord, [0,{@link Chunk#SIZE}]
     * @param metadata The metadata to be set, [0,127]
     * Package private
     */
    void setMetadata(int x, int y, int z, byte metadata)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
        {
            this.tiles[coord] = (this.tiles[coord] & TILE_MASK) | (((metadata) << (8*3) ) & META_MASK);
        }
    }

    /**
     * Increments the metadata at the coordinate by 1
     * @param x    x coord, [0,{@link Chunk#SIZE}]
     * @param y    y coord, [0,{@link Chunk#SIZE}]
     * @param z    z coord, [0,{@link Chunk#SIZE}]
     * If the value being incremented is 127, it should wrap around to 0
     * Package private
     */
    void incrementMetadata(int x, int y, int z)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
        {
            this.tiles[coord] = (this.tiles[coord] & TILE_MASK) | (((this.tiles[coord] >>> (8*3))+1 << (8*3)) & META_MASK);
        }
    }

    /**
     * Decrements the metadata at the coordinate by 1
     * @param x    x coord, [0,{@link Chunk#SIZE}]
     * @param y    y coord, [0,{@link Chunk#SIZE}]
     * @param z    z coord, [0,{@link Chunk#SIZE}]
     * If the value being decremented is 0, it should wrap around to 127
     * Package private
     */
    void decrementMetadata(int x, int y, int z)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
        {
            this.tiles[coord] = (this.tiles[coord] & TILE_MASK) | (((this.tiles[coord] >>> (8*3))-1 << (8*3)) & META_MASK);
        }
    }

    boolean isEmpty()
    {
        return Arrays.stream(this.tiles).allMatch((i) -> i == 0);
    }

    @Override
    public String toString()
    {
        return "Chunk" + position;
    }

    /**
     *
     * @return a set of boolean values describing where solid tiles are
     */
    boolean[][][] getCollider()
    {
        boolean[][][] collider = new boolean[SIZE][SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                for (int k = 0; k < SIZE; k++)
                {
                    collider[i][j][k] = !getTile(i,j,k).isAir();
                }
            }
        }
        return collider;
    }
}
