package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.model.tileenitites.TileEntity;
import com.deadvikingstudios.norsetown.model.world.gen.PerlinNoise;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * Chunks are 3D arrays of Tiles that shape the terrain
 *
 */
public class Chunk
{
    /**
     * Horizontal Axis, CHUNK_SIZE*CHUNK_SIZE tiles in a vertical slice
     */
    public static final int CHUNK_SIZE = 16;
    /**
     * Vertical Axis, how many vertical slices are in a chunk
     */
    public static final int CHUNK_HEIGHT = 16;

    protected float posX, posY, posZ;

    protected int[][][] tiles;

    /**
     * Tile Entities are stored per chunk, and are self aware.
     * when a tile the provides a tile entity is placed, the function createTileEntity() returns a new instance of the
     * TileEntity. When a world is loaded, TileEntities that were saved are attached to the tiles at their respective
     * locations, provided that tile is a tileEntityProvider.
     *
     */
    public List<TileEntity> tileEntityList = new ArrayList<TileEntity>();

    /**
     * Default Constructor
     * @param x x position of the west face
     * @param y y position of the bottom face
     * @param z z position of the south face
     */
    public Chunk(int x, int y, int z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;

        this.tiles = new int[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];
        init();
    }

    protected void init()
    {
        for (int i = 0; i < CHUNK_SIZE; ++i)
        {

            for (int k = 0; k < CHUNK_SIZE; ++k)
            {
                for (int j = 0; j < PerlinNoise.perlin(
                        (this.posX + i)*0.1f,
                        (this.posY + j)*0.1f,
                        (this.posZ + k)*0.1f) * CHUNK_SIZE; //multiply by vertical distribution
                     ++j)
                {
                    if(j < 0 || j >= CHUNK_HEIGHT)
                    {
                        continue;
                    }
                    tiles[i][j][k] = 2;
                }
                for (int j = CHUNK_HEIGHT - 1; j >= 0; --j)
                {
                    if(tiles[i][j][k] == 2)
                    {
                        tiles[i][j][k] = 1;
                        break;
                    }
                }
            }

        }
    }

    public void update(float dt)
    {

    }

    public float getPosX()
    {
        return posX;
    }

    public float getPosY()
    {
        return posY;
    }

    public float getPosZ()
    {
        return posZ;
    }

    public Vector3f getPosition()
    {
        return new Vector3f(posX,posY,posZ);
    }

    public ArrayList<TileEntity> getTileEntities()
    {
        return (ArrayList)this.tileEntityList;
    }


    /*public int getTileAt(float x, float y, float z)
    {
        return getTile((int)Math.ceil(x * (1f/TILE_SIZE)), (int)Math.ceil(y * (1f/TILE_HEIGHT)), (int)Math.ceil(z * (1f/TILE_SIZE)) );
    }*/

    /**
     * Takes in coordinates within the tile
     *
     * TODO Have a method in world that converts world coordinates into chunk coordinates
     * TODO as tiles within chunks are scaled
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getTileAt(int x, int y, int z)
    {
        if(!coordsWithinChunk(x,y,z))
        {
            //TODO replace with a call back to world(?)
            return 0;
        }
        return tiles[x][y][z];
    }

    /**
     * Checks if the coordinates specified are within the chunk
     * @param x
     * @param y
     * @param z
     * @return true if coordinates are within bounds, false if not
     */
    private boolean coordsWithinChunk(int x, int y, int z)
    {
        if(x < 0 || x >= CHUNK_SIZE || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_SIZE)
        {
            return false;
        }
        return true;
    }

    /**
     * Sets the tile at coords to the tile parameter if it is within bounds
     * @param tile
     * @param x
     * @param y
     * @param z
     */
    public void setTile(Tile tile, int x, int y, int z)
    {
        if(coordsWithinChunk(x,y,z))
        {
            tiles[x][y][z] = tile.getIndex();
        }
    }

    /**
     * Sets the tile at coords to Air if it is within bounds
     * @param x
     * @param y
     * @param z
     */
    public void setAir(int x, int y, int z)
    {
        if(coordsWithinChunk(x,y,z))
        {
            tiles[x][y][z] = 0;
        }
    }

}
