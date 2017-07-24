package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.model.tiles.EnumTileShape;
import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.tileenitites.TileEntity;
import com.deadvikingstudios.norsetown.model.world.gen.PerlinNoise;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * Chunks are 3D arrays of Tiles that shape the terrain
 *
 */
public class Chunk extends Entity
{
    /**
     * Horizontal Axis, CHUNK_SIZE*CHUNK_SIZE tiles in a vertical slice
     */
    public static final int CHUNK_SIZE = 16;
    /**
     * Vertical Axis, how many vertical slices are in a chunk
     */
    public static final int CHUNK_HEIGHT = 16;

    protected byte[][][] tiles;
    protected byte[][][] metadata;
    //This is what it saves to a file
    protected Map<Vector3f, TileData> blocksChanged = new HashMap<>();

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
        super();
        this.position = new Vector3f(x,y,z);
        if(GameContainer.MODE == "debug") System.out.println("Chunk created at: " + position + "; Containing " + CHUNK_SIZE * CHUNK_HEIGHT * CHUNK_SIZE + "tiles.");

        this.tiles = new byte[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];
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
                float h = CHUNK_SIZE*World.CHUNK_NUM_XZ - (float)Math.sqrt(
                        (((CHUNK_SIZE*World.CHUNK_NUM_XZ*0.5f)-(this.getPosX()+i))*((CHUNK_SIZE*World.CHUNK_NUM_XZ*0.5f)-(this.getPosX()+i)))
                        +(((CHUNK_SIZE*World.CHUNK_NUM_XZ*0.5f)-(this.getPosZ()+k))*((CHUNK_SIZE*World.CHUNK_NUM_XZ*0.5f)-(this.getPosZ()+k)))
                );

                //TODO move this to a terrain generator

                int terrainHeight = (int)(PerlinNoise.perlin(
                        (seedVal + this.position.x / Tile.TILE_SIZE + i),//*Tile.TILE_SIZE*0.025f,
                        (seedVal + this.position.y / Tile.TILE_HEIGHT),//*Tile.TILE_HEIGHT*0.025f,
                        (seedVal + this.position.z / Tile.TILE_SIZE + k))//*Tile.TILE_SIZE*0.025f) * CHUNK_HEIGHT/4f)
                        /*+
                        (PerlinNoise.perlin(
                        (seedVal + this.position.x / Tile.TILE_SIZE + i)*Tile.TILE_SIZE*0.05f,
                        (seedVal + this.position.y / Tile.TILE_HEIGHT + j)*Tile.TILE_HEIGHT*0.05f,
                        (seedVal + this.position.z / Tile.TILE_SIZE + k)*Tile.TILE_SIZE*0.05f) * CHUNK_HEIGHT/16f) +
                        (PerlinNoise.perlin(
                        (seedVal + this.position.x / Tile.TILE_SIZE + i)*Tile.TILE_SIZE*0.1f,
                        (seedVal + this.position.y / Tile.TILE_HEIGHT + j)*Tile.TILE_HEIGHT*0.1f,
                        (seedVal + this.position.z / Tile.TILE_SIZE + k)*Tile.TILE_SIZE*0.1f) * CHUNK_HEIGHT/32f)*/
                        + h);

                for (int j = (int)this.getPosY(); j < terrainHeight+this.getPosY();// + CHUNK_HEIGHT*0.5f;
                     ++j)
                {
                    //System.out.println("j");
                    if(j+this.getPosY() < this.getPosY() || j >= CHUNK_HEIGHT*World.CHUNK_NUM_Y)
                    {
                        continue;
                    }
                    setTile(Tile.Tiles.tileStoneCliff, i, j, k);
                }
                //local coords TODO: move to terrain decorator
                for (int j = CHUNK_HEIGHT - 1; j >= 0; --j)
                {
                    if(tiles[i][j][k] != 0)
                    {
                        if(j >= World.SEA_LEVEL+World.BEACH_HEIGHT)
                        {
                            setTile(Tile.Tiles.tileGrass, i, j, k);
                            if (World.getWorld().getRandom().nextBoolean())
                            {
                                setTile(Tile.Tiles.tileGrassTall, i, j + 1, k);

                            }
                            setTile(Tile.Tiles.tileSoil, i, j-1, k);
                            setTile(Tile.Tiles.tileSoil, i, j-2, k);
                            setTile(Tile.Tiles.tileSoil, i, j-3, k);
                            setTile(Tile.Tiles.tileSoil, i, j-4, k);
                            setTile(Tile.Tiles.tileSoil, i, j-5, k);
                        }
                        else
                        {
                            setTile(Tile.Tiles.tileClay, i, j, k);
                            setTile(Tile.Tiles.tileClay, i, j-1, k);
                            setTile(Tile.Tiles.tileClay, i, j-2, k);
                            setTile(Tile.Tiles.tileClay, i, j-3, k);
                            setTile(Tile.Tiles.tileClay, i, j-4, k);
                            setTile(Tile.Tiles.tileClay, i, j-5, k);
                        }

                        setTile(Tile.Tiles.tileClay, i, j-6, k);
                        setTile(Tile.Tiles.tileClay, i, j-7, k);
                        setTile(Tile.Tiles.tileClay, i, j-8, k);
                        break;
                    }
                }
                //setTile(Tile.Tiles.tilePlank, i, Chunk.CHUNK_HEIGHT-1, k);
            }
        }
    }

    public void update(World world)
    {
        Random random = new Random();
        for (int n = 0; n < World.chunkTickSpeed; n++)
        {
            int i = random.nextInt(CHUNK_SIZE);
            int j = random.nextInt(CHUNK_HEIGHT);
            int k = random.nextInt(CHUNK_SIZE);
            //world, not tilegrid coords;
            Tile.Tiles.get(tiles[i][j][k]).update(world, (int)((this.getPosX()+i)), (int)((this.getPosY()+j)), (int)((this.getPosZ()+k)));
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

    public Vector3f getChunkPosition()
    {
        return new Vector3f(position);
    }

    public ArrayList<TileEntity> getTileEntities()
    {
        return (ArrayList)this.tileEntityList;
    }

    private boolean isCoordWithinChunk(int x, int y, int z)
    {
        if(x < 0 || x >= CHUNK_SIZE || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_SIZE)
        {
            return false;
        }
        return true;
    }

    private boolean isCoordWithinChunk(Vector3f pos)
    {
        return isCoordWithinChunk((int)pos.x, (int)pos.y, (int)pos.z);
    }

    public int getTile(int x, int y, int z)
    {
        if(!isCoordWithinChunk(x,y,z))
        {
            //TODO replace with a call back to world(?)
            return 0;
        }
        return tiles[x][y][z];
    }

    public int getTile(Vector3f pos)
    {
        return getTile((int)pos.x, (int)pos.y, (int)pos.z);
    }

    public void setTile(Tile tile, int x, int y, int z)
    {
        this.setTile(tile, x,y,z,0);
    }

    public void setTile(Tile tile, int x, int y, int z, int metadataIn)
    {
        if(isCoordWithinChunk(x,y,z))
        {
            tiles[x][y][z] = (byte)(tile.getIndex()%Byte.MAX_VALUE);
            metadata[x][y][z] = (byte)metadataIn;
            this.flagForReMesh = true;
        }
    }

    public void setTile(Tile tile, Vector3f pos)
    {
        this.setTile(tile, (int)pos.x, (int)pos.y, (int)pos.z);
    }

    public void setTile(Tile tile, Vector3f pos, int metadataIn)
    {
        this.setTile(tile, (int)pos.x, (int)pos.y, (int)pos.z, metadataIn);
    }

    public int getMetadata(int x, int y, int z)
    {
        if(isCoordWithinChunk(x,y,z))
        {
            return metadata[x][y][z];
        }
        return 0;
    }

    public int getMetadata(Vector3f pos)
    {
        return this.getMetadata((int)pos.x, (int)pos.x, (int)pos.z);
    }

    public void setMetadata(int x, int y, int z, int metadataIn)
    {
        //System.out.println(x+","+y+","+z);
        if(isCoordWithinChunk(x,y,z))
        {
            //System.out.println();
            //System.out.println(metadata[x][y][z]);
            EnumTileShape rType = Tile.Tiles.get(tiles[x][y][z]).getTileShape(metadata[x][y][z]);
            metadata[x][y][z] = (byte)(metadataIn);
            if(rType != Tile.Tiles.get(tiles[x][y][z]).getTileShape(metadataIn))
            {
                this.flagForReMesh = true;
            }
            //System.out.println(metadata[x][y][z]);
        }
    }

    public void setMetadata(Vector3f pos, int metadataIn)
    {
        this.setMetadata((int)pos.x, (int)pos.x, (int)pos.z, metadataIn);
    }

    public void setAir(int x, int y, int z)
    {
        this.setTile(Tile.Tiles.tileAir, x, y, z);
    }

    public boolean isEmpty()
    {
        return Arrays.deepEquals(tiles, new byte[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE])
                && Arrays.deepEquals(metadata, new byte[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE]);
    }

    public class TileData
    {
        public byte id, metadata;

        public TileData(byte id, byte metadata)
        {
            this.id = id;
            this.metadata = metadata;
        }

        @Override
        public String toString()
        {
            return this.id+"_"+this.metadata;
        }

        @Override
        public int hashCode()
        {
            return (((int)this.id) << 8 & 0x0000ff00) | (((int)this.metadata) & 0x000000ff);
        }
    }

    public class TilePos
    {
        public byte x, y, z;

        public TilePos(byte x, byte y, byte z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString()
        {
            return x+"_"+y+"_"+z;
        }

        @Override
        public int hashCode()
        {
            return (((int)this.x) << 16 & 0x00ff0000) | (((int)this.y) << 8 & 0x0000ff00) | (((int)this.z) & 0x000000ff);
        }
    }
}
