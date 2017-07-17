package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.tileenitites.TileEntity;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class World
{


    private Chunk[][][] chunkList;//List<Chunk> chunkList = new ArrayList<Chunk>();
    public static final int CHUNK_NUM_XZ = 16, CHUNK_NUM_Y = 1;
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
                    chunkList[i][j][k] = new Chunk(i*Chunk.CHUNK_SIZE*Tile.TILE_SIZE,
                            j*Chunk.CHUNK_HEIGHT*Tile.TILE_HEIGHT,k*Chunk.CHUNK_SIZE*Tile.TILE_SIZE);
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

    public Chunk getChunkAt(float x, float y, float z)
    {
        if(x < 0 || x >= CHUNK_NUM_XZ * Chunk.CHUNK_SIZE
                || y < 0 || y >= CHUNK_NUM_Y * Chunk.CHUNK_HEIGHT
                || z < 0 || z >= CHUNK_NUM_XZ * Chunk.CHUNK_SIZE)
        {
            return null;
        }

        return this.chunkList[((int)x)/Chunk.CHUNK_SIZE][((int)y)/Chunk.CHUNK_HEIGHT][((int)z)/Chunk.CHUNK_SIZE];
    }

    public List<Entity> getEntities()
    {
        return entityList;
    }

    public Chunk getEmptyChunk()
    {
        return emptyChunk;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return the tile at Worldspace coords
     */
    public int getTileAt(float x, float y, float z)
    {
        return this.getTileAt(new Vector3f(x,y,z));
    }

    /**
     *
     * @param position
     * @return the tile at Worldspace coords
     */
    public int getTileAt(Vector3f position)
    {
        //System.out.println(x / Chunk.CHUNK_SIZE + ", " + y / Chunk.CHUNK_HEIGHT + ", " + z / Chunk.CHUNK_SIZE);
        Chunk chunk = getChunkAt(position.x, position.y, position.z);
        if(chunk != null)
        {
            Vector3f tileCoords = worldSpaceToTileCoords(position);
            return chunk.getTileAt(((int)tileCoords.x) % Chunk.CHUNK_SIZE,
                    ((int)tileCoords.y) % Chunk.CHUNK_HEIGHT, ((int)tileCoords.z) % Chunk.CHUNK_SIZE);
        }
        return 0;
    }

    /**
     * @param position
     * @return the Tile at Tilespace coords
     */
    public int getTile(Vector3f position)
    {
        //System.out.println(x / Chunk.CHUNK_SIZE + ", " + y / Chunk.CHUNK_HEIGHT + ", " + z / Chunk.CHUNK_SIZE);
        Chunk chunk = getChunkAt(position.x, position.y, position.z);
        if(chunk != null)
        {
            //Vector3f tileCoords = worldSpaceToTileCoords(position);
            return chunk.getTileAt(((int)position.x) % Chunk.CHUNK_SIZE,
                    ((int)position.y) % Chunk.CHUNK_HEIGHT, ((int)position.z) % Chunk.CHUNK_SIZE);
        }
        return 0;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return the tile at Tilespace coords
     */
    public int getTile(int x, int y, int z)
    {
        return this.getTile(new Vector3f(x,y,z));
    }

    public void setTile(Tile tile, int x, int y, int z)
    {
        Chunk chunk = getChunkAt(x, y, z);
        if(chunk != null)
        {
            //Vector3f tileCoords = worldSpaceToTileCoords(x,y,z);
            chunk.setTile(tile, ((int)x) % Chunk.CHUNK_SIZE,
                    ((int)y) % Chunk.CHUNK_HEIGHT, ((int)z) % Chunk.CHUNK_SIZE);
        }
    }

    public void setTileAt(Tile tile, int x, int y, int z)
    {
        Chunk chunk = getChunkAt(x, y, z);
        if(chunk != null)
        {
            Vector3f tileCoords = worldSpaceToTileCoords(x,y,z);
            chunk.setTile(tile, ((int)tileCoords.x) % Chunk.CHUNK_SIZE,
                    ((int)tileCoords.y) % Chunk.CHUNK_HEIGHT, ((int)tileCoords.z) % Chunk.CHUNK_SIZE);
        }
    }

    /**
     * Transforms a position in World space into the Local Space relative to the tilegrid
     * @param position
     * @return
     */
    public Vector3f worldSpaceToTileCoords(Vector3f position)
    {
        return new Vector3f((int)(Math.round(position.x)/Tile.TILE_SIZE),
                (int)(Math.round(position.y)/Tile.TILE_HEIGHT), (int)(Math.round(position.z)/Tile.TILE_SIZE));
    }

    public Vector3f worldSpaceToTileCoords(float x, float y, float z)
    {
        return this.worldSpaceToTileCoords(new Vector3f(x,y,z));
    }
}
