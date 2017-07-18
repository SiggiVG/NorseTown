package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.controller.GameContainer;
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
    public static final int CHUNK_HEIGHT = 256;

    protected Vector3f position;

    protected int[][][] tiles;
    protected byte[][][] metadata;

    /**
     * Tile Entities are stored per chunk, and are self aware.
     * when a tile the provides a tile entity is placed, the function createTileEntity() returns a new instance of the
     * TileEntity. When a world is loaded, TileEntities that were saved are attached to the tiles at their respective
     * locations, provided that tile is a tileEntityProvider.
     *
     */
    public List<TileEntity> tileEntityList = new ArrayList<TileEntity>();
    private boolean flagForReMesh = false;

    public boolean isFlagForReMesh()
    {
        return flagForReMesh;
    }

    public void setFlagForReMesh(boolean flagForReMesh)
    {
        this.flagForReMesh = flagForReMesh;
    }

    /**
     * Default Constructor
     * @param x x position of the west face
     * @param y y position of the bottom face
     * @param z z position of the south face
     */
    public Chunk(float x, float y, float z)
    {
        this.position = new Vector3f(x,y,z);
        if(GameContainer.MODE == "debug") System.out.println("Chunk created at: " + position + "; Containing " + CHUNK_SIZE * CHUNK_HEIGHT * CHUNK_SIZE + "tiles.");

        this.tiles = new int[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];
        this.metadata = new byte[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];
        init();
    }

    protected void init()
    {
        float seedVal = 0;//(float)World.getWorld().SEED / 256f;
        //fill with terrain
        for (int i = 0; i < CHUNK_SIZE; ++i)
        {
            for (int k = 0; k < CHUNK_SIZE; ++k)
            {
                float h = 0;
                //X is bigger
                if(this.getPosX()+i > World.CHUNK_NUM_XZ*CHUNK_SIZE/2)
                {
                    //both are bigger
                    if(this.getPosZ()+k > World.CHUNK_NUM_XZ*CHUNK_SIZE/2)
                    {
                        h = Math.min(World.CHUNK_NUM_XZ*CHUNK_SIZE-(this.getPosX()+i), World.CHUNK_NUM_XZ*CHUNK_SIZE-(this.getPosZ()+k));
                    }
                    //only X is bigger
                    else
                    {
                        h = Math.min(World.CHUNK_NUM_XZ*CHUNK_SIZE-(this.getPosX()+i), this.getPosZ()+k);
                    }
                }
                //Z is bigger
                else if(this.getPosZ()+k > World.CHUNK_NUM_XZ*CHUNK_SIZE/2)
                {
                    //both are bigger
                    if(this.getPosX()+i > World.CHUNK_NUM_XZ*CHUNK_SIZE/2)
                    {
                        h = Math.min(World.CHUNK_NUM_XZ*CHUNK_SIZE-(this.getPosX()+i), World.CHUNK_NUM_XZ*CHUNK_SIZE-(this.getPosZ()+k));
                    }
                    //only Z is bigger
                    else
                    {
                        h = Math.min(this.getPosX()+i, World.CHUNK_NUM_XZ*CHUNK_SIZE-(this.getPosZ()+k));
                    }
                }
                //neither are bigger
                else
                {
                    h = Math.min(this.getPosX()+i, this.getPosZ()+k);
                }

                h*=0.75;




                for (int j = 0; j < (PerlinNoise.perlin(
                        (seedVal + this.position.x / Tile.TILE_SIZE + i)*Tile.TILE_SIZE*0.025f,
                        (seedVal + this.position.y / Tile.TILE_HEIGHT + j)*Tile.TILE_HEIGHT*0.025f,
                        (seedVal + this.position.z / Tile.TILE_SIZE + k)*Tile.TILE_SIZE*0.025f) * CHUNK_HEIGHT/4f) +
                        (PerlinNoise.perlin(
                        (seedVal + this.position.x / Tile.TILE_SIZE + i)*Tile.TILE_SIZE*0.05f,
                        (seedVal + this.position.y / Tile.TILE_HEIGHT + j)*Tile.TILE_HEIGHT*0.05f,
                        (seedVal + this.position.z / Tile.TILE_SIZE + k)*Tile.TILE_SIZE*0.05f) * CHUNK_HEIGHT/16f) +
                        (PerlinNoise.perlin(
                        (seedVal + this.position.x / Tile.TILE_SIZE + i)*Tile.TILE_SIZE*0.1f,
                        (seedVal + this.position.y / Tile.TILE_HEIGHT + j)*Tile.TILE_HEIGHT*0.1f,
                        (seedVal + this.position.z / Tile.TILE_SIZE + k)*Tile.TILE_SIZE*0.1f) * CHUNK_HEIGHT/32f)
                        + 0.75f*(h); //multiply by vertical distribution
                     ++j)
                {
                    if(j < 0 || j >= CHUNK_HEIGHT)
                    {
                        continue;
                    }
                    setTile(Tile.Tiles.tileStoneCliff, i, j, k);
                }
                for (int j = CHUNK_HEIGHT - 1; j >= 0; --j)
                {
                    if(tiles[i][j][k] != 0)
                    {
                        if(World.getWorld().getRandom().nextBoolean())
                        {
                            setTile(Tile.Tiles.tileGrassTall, i, j+1, k);
                        }
                        setTile(Tile.Tiles.tileGrass, i, j, k);
                        setTile(Tile.Tiles.tileSoil, i, j-1, k);
                        setTile(Tile.Tiles.tileSoil, i, j-2, k);
                        setTile(Tile.Tiles.tileSoil, i, j-3, k);
                        setTile(Tile.Tiles.tileSoil, i, j-4, k);
                        setTile(Tile.Tiles.tileSoil, i, j-5, k);
                        setTile(Tile.Tiles.tileClay, i, j-6, k);
                        setTile(Tile.Tiles.tileClay, i, j-7, k);
                        setTile(Tile.Tiles.tileClay, i, j-8, k);
                        break;
                    }
                }
            }
        }
    }

    public void update(World world)
    {
        for (int n = 0; n < World.chunkTickSpeed; n++)
        {
            int i = world.getRandom().nextInt(CHUNK_SIZE);
            int j = world.getRandom().nextInt(CHUNK_HEIGHT);
            int k = world.getRandom().nextInt(CHUNK_SIZE);
            //world, not tilegrid coords;
            Tile.Tiles.get(tiles[i][j][k]).update(world, (int)((this.getPosX()+i)*Tile.TILE_SIZE), (int)((this.getPosY()+j)*Tile.TILE_HEIGHT), (int)((this.getPosZ()+k)*Tile.TILE_SIZE));
        }
    }

    public float getPosX()
    {
        return position.x;
    }

    public float getPosY()
    {
        return position.y;
    }

    public float getPosZ()
    {
        return position.z;
    }

    public Vector3f getPosition()
    {
        return new Vector3f(position);
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
     * Tilespace
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

    private boolean coordsWithinChunk(float x, float y, float z)
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
            this.flagForReMesh = true;
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

    public int getMetadataAt(Vector3f pos)
    {
        if(coordsWithinChunk(pos.x,pos.y,pos.z))
        {
            return metadata[(int)pos.x][(int)pos.y][(int)pos.z];
        }
        return 0;
    }

    public void setMetadataAt(Vector3f pos, int val)
    {
        if(coordsWithinChunk(pos.x,pos.y,pos.z))
        {
            metadata[(int)pos.x][(int)pos.y][(int)pos.z] = (byte)(val % Byte.MAX_VALUE);
            if(Tile.Tiles.get(tiles[(int)pos.x][(int)pos.y][(int)pos.z]).getRenderType() != 0)
            {
                this.flagForReMesh = true;
            }
        }
    }
}
