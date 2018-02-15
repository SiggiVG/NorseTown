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
@Deprecated
public class WorldOld
{


    public static int chunkTickSpeed = 4;
    private Chunk[][] chunkList;//List<Chunk> chunkList = new ArrayList<Chunk>();
    public static final int CHUNK_NUM_XZ = 8;
    //public static final int CHUNK_NUM_Y = 1; //setting to values other than 1 currently breaks rendering
    public static final float CHUNK_OFFSET_Y = 0;//Tile.TILE_HEIGHT;
    public static final float CHUNK_OFFSET_XZ = 0;//Tile.TILE_SIZE;
    public static final float SEA_LEVEL = 7.6f;
    public static final float BEACH_HEIGHT = 3.4f;

    public List<Entity> entityList = new ArrayList<Entity>();

    private static WorldOld instance;
    private Random random;
    public final int SEED;

    ///private boolean[] coastSides = new boolean[8];
    private EnumDirection coastSide = EnumDirection.SOUTH;

    public WorldOld()
    {
        this((int)System.nanoTime() % Integer.MAX_VALUE);
    }

    public static WorldOld getWorld()
    {
        return instance;
    }

    public Random getRandom()
    {
        return random;
    }

    /**
     * @return the number of chunks that are not null and are not empty
     */
    public int getNumberOfNonEmptyChunks()
    {
        int chunkNum = 0;
        for (int i = 0; i < CHUNK_NUM_XZ; i++)
        {
            for (int k = 0; k < CHUNK_NUM_XZ; k++)
            {
                //for (int j = 0; j < CHUNK_NUM_Y; j++)
                {
                    if(chunkList[i][k] != null)
                    if(!chunkList[i][k].isEmpty())
                    {
                        ++chunkNum;
                    }
                }
            }
        }
        return chunkNum;
    }

    /**
     * @return the number of chunks that can be stored in the 3d array
     */
    public int getMaxPossibleNumberOfChunks()
    {
        return CHUNK_NUM_XZ*CHUNK_NUM_XZ;//*CHUNK_NUM_Y;
    }

    public WorldOld(int seed)
    {
        this.SEED = seed;
        random = new Random(seed);
        instance = this;

        chunkList = new Chunk[CHUNK_NUM_XZ][CHUNK_NUM_XZ];

        for (int i = 0; i < CHUNK_NUM_XZ; i++)
        {
            //for (int j = 0; j < CHUNK_NUM_Y; j++)
            {
                for (int k = 0; k < CHUNK_NUM_XZ; k++)
                {
                    Chunk chunk = new Chunk(i*Chunk.CHUNK_SIZE*Tile.TILE_SIZE,
                            0,k*Chunk.CHUNK_SIZE*Tile.TILE_SIZE, 8);
                    if(!chunk.isEmpty()) chunkList[i][k] = chunk;
                    //System.out.println(chunk.getPosY());
                }
            }
        }
    }


    public Chunk[][] getChunkList()
    {
        return this.chunkList;
    }

    /**
     * use to get the chunk at the x,y,z index within the chunk array
     * @param x
     * @param y
     * @param z
     * @return The chunk at x,y,z in the chunk array
     */
    public Chunk getChunkAtIndex(int x, int y, int z)
    {
        if(x < 0 || x >= CHUNK_NUM_XZ /*|| y < 0 || y >= CHUNK_NUM_Y*/ || z < 0 || z >= CHUNK_NUM_XZ)
        {
            return null;
        }

        return this.chunkList[x][z];
    }

    /**
     * use to get the chunk that contains tilespace coords x,y,z
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Chunk getChunk(int x, int y, int z)
    {
        return this.getChunkAtIndex(x/Chunk.CHUNK_SIZE,0,z/Chunk.CHUNK_SIZE);
    }

    /**
     * use to get the chunk that contains tilespace coords x,y,z
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Chunk getChunk(float x, float y, float z)
    {
        return getChunk((int)x, (int)y, (int)z);
    }

    /**
     * use to get the chunk that contains tilespace coords pos.x,pos.y,pos.z
     * @param pos
     * @return
     */
    public Chunk getChunk(Vector3f pos)
    {
        return getChunk((int)pos.x, (int)pos.y, (int)pos.z);
    }

    /**
     * use to get the chunk that contains worldspace coords x,y,z
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Chunk getChunkAt(int x, int y, int z)
    {
        return getChunk(worldSpaceToTileCoords(new Vector3f(x,y,z)));
    }

    /**
     * use to get the chunk that contains worldspace coords x,y,z
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Chunk getChunkAt(float x, float y, float z)
    {
        return getChunk(worldSpaceToTileCoords(new Vector3f(x,y,z)));
    }

    /**
     * use to get the chunk that contains worldspace coords pos.x,pos.y,pos.z
     * @param pos
     * @return
     */
    public Chunk getChunkAt(Vector3f pos)
    {
        return getChunk(worldSpaceToTileCoords(pos));
    }


    public List<Entity> getEntities()
    {
        return entityList;
    }

    public Chunk getEmptyChunk(float x, float y, float z)
    {
        return new EmptyChunk(x,y,z);
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
                    ((int)tileCoords.y), ((int)tileCoords.z) % Chunk.CHUNK_SIZE);
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
            return chunk.getTile((x) % Chunk.CHUNK_SIZE, (y), (z) % Chunk.CHUNK_SIZE);
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
                            ((int) y), ((int) z) % Chunk.CHUNK_SIZE);
                }
            }
            else
            {
                chunk.setTile(tile, ((int) x) % Chunk.CHUNK_SIZE,
                        ((int) y), ((int) z) % Chunk.CHUNK_SIZE);
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
        if(chunk != null) //TODO create a new chunk if one does not exist
        {
            Vector3f tileCoords = worldSpaceToTileCoords(x,y,z);
            if(checkReplacable)
            {
                if (Tile.Tiles.get(getTile(tileCoords)).isReplacable())
                {
                    chunk.setTile(tile, ((int) tileCoords.x) % Chunk.CHUNK_SIZE,
                            ((int) tileCoords.y), ((int) tileCoords.z) % Chunk.CHUNK_SIZE);
                }
            }
            else
            {
                chunk.setTile(tile, ((int) tileCoords.x) % Chunk.CHUNK_SIZE,
                        ((int) tileCoords.y), ((int) tileCoords.z) % Chunk.CHUNK_SIZE);
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
     * Transforms a position in WorldOld space into the Local Space relative to the tilegrid
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
     * Transforms a position in Local Space relative to the tilegrid into WorldOld Space
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
                //for (int j = 0; j < CHUNK_NUM_Y; ++j)
                {
                    if(chunkList[i][k] != null)
                    chunkList[i][k].update(getWorld());
                }
            }
        }

    }

    /**
     * WorldOld space
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
     * WorldOld space
     * @param metadataIn
     * @param x
     * @param y
     * @param z
     */
    public void setMetadataAt(int metadataIn, int x, int y, int z)
    {
        Chunk chunk = getChunkAt(worldSpaceToTileCoords(x, y, z));
        if(chunk != null)
        {
            chunk.setMetadata(worldSpaceToTileCoords(x,y,z), metadataIn);
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
            return chunk.getMetadata(x%Chunk.CHUNK_SIZE,y,z%Chunk.CHUNK_SIZE);
        }
        return 0;
    }

    /**
     * Tilespace
     * @param metadataIn
     * @param x
     * @param y
     * @param z
     */
    public void setMetadata(int metadataIn, int x, int y, int z)
    {
        Chunk chunk = getChunk(x,y,z);
        if(chunk != null)
        {
            chunk.setMetadata(x%Chunk.CHUNK_SIZE,y,z%Chunk.CHUNK_SIZE, metadataIn);
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
            setMetadata(getMetadata(x,y,z)+1, x,y,z);
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
            int metadata = getMetadata(x,y,z);
            if(metadata > 0)
            {
                setMetadata(metadata-1, x, y, z );
            }
        }
    }
}
