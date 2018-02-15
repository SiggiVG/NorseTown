package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.Vector3i;

import java.io.Serializable;
import java.util.Arrays;

public class Chunk implements Serializable
{
    public final Vector3i position;

    public final static byte SIZE = 16;
    public final static int SIZE_CUBED = SIZE * SIZE * SIZE;

    private int[] tiles;

    Chunk(Chunk chunk)
    {
        this.position = new Vector3i(chunk.position);
        this.tiles = Arrays.copyOf(chunk.tiles, SIZE_CUBED);
    }

    Chunk(Vector3i position)
    {
        this.position = new Vector3i(position);
        this.tiles = new int[SIZE_CUBED];
        //Logger.debug("Structure Chunk created at " + position.x + "," + position.y + "," + position.z);
    }

    public Vector3i getRenderPosition()
    {
        return new Vector3i(this.position.x * SIZE,this.position.y * SIZE,this.position.z * SIZE);
    }

    private int getCoord(int x, int y, int z)
    {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && z >= 0 && z < SIZE)
        {
            return x + (y * SIZE) + (z*SIZE*SIZE);
        }
        return -1;
    }

    /**
     * Package private
     *
     * @param x x coord, 0:15
     * @param y y coord, 0:15
     * @param z z coord, 0:15
     * @return the tile at the location
     */
    Tile getTile(int x, int y, int z)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
            return Tile.Tiles.get(tiles[coord]);
        return Tile.Tiles.tileAir;
    }

    /**
     * Package private
     *
     * @param tile The tile being set
     * @param x    x coord, 0:15
     * @param y    y coord, 0:15
     * @param z    z coord, 0:15
     */
    boolean setTile(Tile tile, int x, int y, int z)
    {
        int coord = getCoord(x,y,z);
        if(coord >= 0 && coord < SIZE_CUBED)
        {
            //Moved same tile check to ChunkColumn
            this.tiles[coord] = tile.getIndex();
            return true;
        }
        return false;
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
}
