package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.model.entities.Entity;
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


    public static int chunkTickSpeed = 16;
    private Chunk[][][] chunkList;//List<Chunk> chunkList = new ArrayList<Chunk>();
    public static final int CHUNK_NUM_XZ = 1, CHUNK_NUM_Y = 1;
    public static final float CHUNK_OFFSET_Y = 0;//Tile.TILE_HEIGHT;
    public static final float CHUNK_OFFSET_XZ = 0;//Tile.TILE_SIZE;
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
    public Chunk getChunkAtIndex(int x, int y, int z)
    {
        if(x < 0 || x >= CHUNK_NUM_XZ || y < 0 || y >= CHUNK_NUM_Y || z < 0 || z >= CHUNK_NUM_XZ)
        {
            return null;
        }

        return this.chunkList[x][y][z];
    }

    public Chunk getChunk(int x, int y, int z)
    {
        if(x < 0 || x >= CHUNK_NUM_XZ*Chunk.CHUNK_SIZE || y < 0 || y >= CHUNK_NUM_Y *Chunk.CHUNK_HEIGHT|| z < 0 || z >= CHUNK_NUM_XZ*Chunk.CHUNK_SIZE)
        {
            return null;
        }

        return this.chunkList[x/Chunk.CHUNK_SIZE][y/Chunk.CHUNK_HEIGHT][z/Chunk.CHUNK_SIZE];
    }

    public Chunk getChunk(float x, float y, float z)
    {
        return getChunk((int)x, (int)y, (int)z);
    }

    public Chunk getChunk(Vector3f pos)
    {
        return getChunk((int)pos.x, (int)pos.y, (int)pos.z);
    }

    public Chunk getChunkAt(int x, int y, int z)
    {
        return getChunk(worldSpaceToTileCoords(new Vector3f(x,y,z)));
    }

    public Chunk getChunkAt(float x, float y, float z)
    {
        return getChunk(worldSpaceToTileCoords(new Vector3f(x,y,z)));
    }

    public Chunk getChunkAt(Vector3f pos)
    {
        return getChunk(worldSpaceToTileCoords(pos));
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
     * @param position
     * @return the tile at Worldspace coords
     */
    public int getTileAt(Vector3f position)
    {
        //System.out.println(x / Chunk.CHUNK_SIZE + ", " + y / Chunk.CHUNK_HEIGHT + ", " + z / Chunk.CHUNK_SIZE);
        Chunk chunk = getChunkAt(worldSpaceToTileCoords(position));
        if(chunk != null)
        {
            Vector3f tileCoords = worldSpaceToTileCoords(position);
            return chunk.getTile(((int)tileCoords.x) % Chunk.CHUNK_SIZE,
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
        return getTile((int)position.x, (int)position.y, (int)position.z);
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
        //System.out.println(x / Chunk.CHUNK_SIZE + ", " + y / Chunk.CHUNK_HEIGHT + ", " + z / Chunk.CHUNK_SIZE);
        Chunk chunk = getChunk(x, y, z);
        if(chunk != null)
        {
            //Vector3f tileCoords = worldSpaceToTileCoords(position);
            return chunk.getTile((x) % Chunk.CHUNK_SIZE,
                    (y) % Chunk.CHUNK_HEIGHT, (z) % Chunk.CHUNK_SIZE);
        }
        return 0;
    }

    /**
     * Use when within Tilespace
     * @param tile
     * @param x
     * @param y
     * @param z
     * @param checkReplacable
     */
    public void setTile(Tile tile, int x, int y, int z, boolean checkReplacable)
    {
        Chunk chunk = getChunk(x, y, z);
        if(chunk != null)
        {
            if(checkReplacable)
            {
                if (Tile.Tiles.get(getTile(x, y, z)).isReplacable())
                {
                    chunk.setTile(tile, ((int) x) % Chunk.CHUNK_SIZE,
                            ((int) y) % Chunk.CHUNK_HEIGHT, ((int) z) % Chunk.CHUNK_SIZE);
                }
            }
            else
            {
                chunk.setTile(tile, ((int) x) % Chunk.CHUNK_SIZE,
                        ((int) y) % Chunk.CHUNK_HEIGHT, ((int) z) % Chunk.CHUNK_SIZE);
            }
        }
    }

    /**
     * Use when within Tilespace
     * @param tile
     * @param x
     * @param y
     * @param z
     */
    public void setTile(Tile tile, int x, int y, int z)
    {
        this.setTile(tile,x,y,z,false);
    }

    /**
     * Use when in Worldspace
     * @param tile
     * @param x
     * @param y
     * @param z
     * @param checkReplacable
     */
    public void setTileAt(Tile tile, int x, int y, int z, boolean checkReplacable)
    {
        Chunk chunk = getChunkAt(x, y, z);
        if(chunk != null)
        {
            Vector3f tileCoords = worldSpaceToTileCoords(x,y,z);
            if(checkReplacable)
            {
                if (Tile.Tiles.get(getTile(tileCoords)).isReplacable())
                {
                    chunk.setTile(tile, ((int) tileCoords.x) % Chunk.CHUNK_SIZE,
                            ((int) tileCoords.y) % Chunk.CHUNK_HEIGHT, ((int) tileCoords.z) % Chunk.CHUNK_SIZE);
                }
            }
            else
            {
                chunk.setTile(tile, ((int) tileCoords.x) % Chunk.CHUNK_SIZE,
                        ((int) tileCoords.y) % Chunk.CHUNK_HEIGHT, ((int) tileCoords.z) % Chunk.CHUNK_SIZE);
            }
        }
    }

    /**
     * Use when in WorldSpace
     * @param tile
     * @param x
     * @param y
     * @param z
     */
    public void setTileAt(Tile tile, int x, int y, int z)
    {
        this.setTileAt(tile,x,y,z, false);
    }

    /**
     * Transforms a position in World space into the Local Space relative to the tilegrid
     * @param position
     * @return
     */
    public Vector3f worldSpaceToTileCoords(Vector3f position)
    {
        return new Vector3f((int)(Math.round(position.x/Tile.TILE_SIZE)),
                (int)(Math.round(position.y/Tile.TILE_HEIGHT)), (int)(Math.round(position.z/Tile.TILE_SIZE)));
    }

    public Vector3f worldSpaceToTileCoords(float x, float y, float z)
    {
        return this.worldSpaceToTileCoords(new Vector3f(x,y,z));
    }

    /**
     * Transforms a position in Local Space relative to the tilegrid into World Space
     * @param position
     * @return
     */
    public Vector3f tileSpaceToWorldCoords(Vector3f position)
    {
        return new Vector3f(position.x * Tile.TILE_SIZE, position.y * Tile.TILE_HEIGHT, position.z * Tile.TILE_SIZE);
    }


    public Vector3f tileSpaceToWorldCoords(float x, float y, float z)
    {
        return this.tileSpaceToWorldCoords(new Vector3f(x,y,z));
    }

    public void update()
    {
        for (int i = 0; i < CHUNK_NUM_XZ; ++i)
        {
            for (int k = 0; k < CHUNK_NUM_XZ; ++k)
            {
                for (int j = 0; j < CHUNK_NUM_Y; ++j)
                {
                    chunkList[i][j][k].update(getWorld());
                }
            }
        }

    }

    /**
     * World space
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getMetadataAt(int x, int y, int z)
    {
        Chunk chunk = getChunkAt(worldSpaceToTileCoords(x, y, z));
        if(chunk != null)
        {
            return chunk.getMetadata(worldSpaceToTileCoords(x,y,z));
        }
        return 0;
    }

    /**
     * World space
     * @param metadataIn
     * @param x
     * @param y
     * @param z
     */
    public void setMetadataAt(int metadataIn, int x, int y, int z)
    {
        metadataIn = metadataIn % Byte.MAX_VALUE;
        Chunk chunk = getChunkAt(worldSpaceToTileCoords(x, y, z));
        if(chunk != null)
        {
            chunk.setMetadata(worldSpaceToTileCoords(x,y,z), (byte)metadataIn);
        }
    }

    /**
     * Tilespace
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getMetadata(int x, int y, int z)
    {
        Chunk chunk = getChunk(x,y,z);
        if(chunk != null)
        {
            return chunk.getMetadata(x,y,z);
        }
        return 0;
    }

    /**
     * Tilespace
     * @param val
     * @param x
     * @param y
     * @param z
     */
    public void setMetadata(int val, int x, int y, int z)
    {
        val = val % Byte.MAX_VALUE;
        Chunk chunk = getChunk(x,y,z);
        if(chunk != null)
        {
            chunk.setMetadata(x,y,z, (byte)val);
        }
    }

    /*public void incrementMetadataAt(int x, int y, int z)
    {
        Chunk chunk = getChunkAt(worldSpaceToTileCoords(x, y, z));
        if(chunk != null)
        {
            chunk.setMetadata(worldSpaceToTileCoords(x,y,z),getMetadataAt(x,y,z)+1);
        }
    }

    public void decrementMetadataAt(int x, int y, int z)
    {
        Chunk chunk = getChunkAt(worldSpaceToTileCoords(x, y, z));
        if(chunk != null)
        {
            int val = getMetadataAt(x,y,z);
            if(val > 0)
            {
                chunk.setMetadata(worldSpaceToTileCoords(x, y, z), val-1);
            }
        }
    }*/

    /**
     * Tilespace
     * @param x
     * @param y
     * @param z
     */
    public void incrementMetadata(int x, int y, int z)
    {
        Chunk chunk = getChunk(x,y,z);
        if(chunk != null)
        {
            //System.out.println();
            //System.out.println(getMetadata(x,y,z));
            chunk.setMetadata(x,y,z,getMetadata(x,y,z)+1);
            //System.out.println(getMetadata(x,y,z));
        }
    }

    /**
     * Tilespace
     * @param x
     * @param y
     * @param z
     */
    public void decrementMetadata(int x, int y, int z)
    {
        Chunk chunk = getChunk(x,y,z);
        if(chunk != null)
        {
            int val = getMetadata(x,y,z);
            if(val > 0)
            {
                chunk.setMetadata(x, y, z, val-1);
            }
        }
    }
}
