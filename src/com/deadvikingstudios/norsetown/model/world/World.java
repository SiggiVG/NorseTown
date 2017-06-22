package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class World
{

    private Chunk[][][] chunkList;//List<Chunk> chunkList = new ArrayList<Chunk>();
    public static final int CHUNK_NUM_XZ = 8, CHUNK_NUM_Y = 1;

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
                    chunkList[i][j][k] = new Chunk(i*Chunk.CHUNK_SIZE,0,k*Chunk.CHUNK_SIZE);
                }
            }
        }
    }


    public Chunk[][][] getChunkList()
    {
        return this.chunkList;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Chunk getChunk(int x, int y, int z)
    {
        if(x < 0 || x >= CHUNK_NUM_XZ || y < 0 || y >= CHUNK_NUM_Y || z < 0 || z >= CHUNK_NUM_XZ)
        {
            return null;
        }

        return this.chunkList[x][y][z];
    }

    public Chunk getEmptyChunk()
    {
        return emptyChunk;
    }

}
