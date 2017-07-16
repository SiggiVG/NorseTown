package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.tileenitites.TileEntity;
import com.deadvikingstudios.norsetown.model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class World
{


    private Chunk[][][] chunkList;//List<Chunk> chunkList = new ArrayList<Chunk>();
    public static final int CHUNK_NUM_XZ = 8, CHUNK_NUM_Y = 1;
    public static final float CHUNK_OFFSET_Y = Tile.TILE_HEIGHT;
    public static final float CHUNK_OFFSET_XZ = Tile.TILE_SIZE;
    private Chunk emptyChunk = new EmptyChunk(0,0,0);

    public List<Entity> entityList = new ArrayList<Entity>();

    private static World instance;
    private Random random;
    public final int SEED;

    public World()
    {
        this((int)System.nanoTime() % Integer.MAX_VALUE);
    }

    public static World getWorld()
    {
        return instance;
    }

    public Random getRandom()
    {
        return random;
    }

    public World(int seed)
    {
        this.SEED = seed;
        random = new Random(seed);
        instance = this;

        chunkList = new Chunk[CHUNK_NUM_XZ][CHUNK_NUM_Y][CHUNK_NUM_XZ];

        for (int i = 0; i < CHUNK_NUM_XZ; i++)
        {
            for (int j = 0; j < CHUNK_NUM_Y; j++)
            {
                for (int k = 0; k < CHUNK_NUM_XZ; k++)
                {
                    chunkList[i][j][k] = new Chunk(i*Chunk.CHUNK_SIZE*Tile.TILE_SIZE,j*Chunk.CHUNK_HEIGHT*Tile.TILE_HEIGHT,k*Chunk.CHUNK_SIZE*Tile.TILE_SIZE);
                }
            }
        }
    }


    public Chunk[][][] getChunkList()
    {
        return this.chunkList;
    }

    /**
     * Depreciates as it is only used when we have a set number of chunks
     * @param x
     * @param y
     * @param z
     * @return The chunk at x,y,z in the chunk array
     */
    @Deprecated
    public Chunk getChunk(int x, int y, int z)
    {
        if(x < 0 || x >= CHUNK_NUM_XZ || y < 0 || y >= CHUNK_NUM_Y || z < 0 || z >= CHUNK_NUM_XZ)
        {
            return null;
        }

        return this.chunkList[x][y][z];
    }

    public Chunk getChunkAt(int x, int y, int z)
    {
        if(x < 0 || x >= CHUNK_NUM_XZ * Chunk.CHUNK_SIZE
                || y < 0 || y >= CHUNK_NUM_Y * Chunk.CHUNK_HEIGHT
                || z < 0 || z >= CHUNK_NUM_XZ * Chunk.CHUNK_SIZE)
        {
            return null;
        }

        return this.chunkList[x/Chunk.CHUNK_SIZE][y/Chunk.CHUNK_HEIGHT][z/Chunk.CHUNK_SIZE];
    }

    public List<Entity> getEntities()
    {
        return entityList;
    }

    public Chunk getEmptyChunk()
    {
        return emptyChunk;
    }

    public int getTileAt(int x, int y, int z)
    {
        //System.out.println(x / Chunk.CHUNK_SIZE + ", " + y / Chunk.CHUNK_HEIGHT + ", " + z / Chunk.CHUNK_SIZE);
        Chunk chunk = getChunkAt(x, y, z);
        if(chunk != null)
        {
            return chunk.getTileAt(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_HEIGHT, z % Chunk.CHUNK_SIZE);
        }
        return 0;
    }

    public void setTile(Tile tile, int x, int y, int z)
    {
        Chunk chunk = getChunkAt(x, y, z);
        if(chunk != null)
        {
            chunk.setTile(tile, x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_HEIGHT, z % Chunk.CHUNK_SIZE);
        }
    }
}
