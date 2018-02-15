package com.deadvikingstudios.norsetown.model.world;

import com.deadvikingstudios.norsetown.model.tiles.EnumTileShape;
import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.tileenitites.TileEntity;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.Logger;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * Chunks are 3D arrays of Tiles that shape the terrain
 *
 */
@Deprecated
public class Chunk extends Entity
{
    /**
     * Horizontal Axis, CHUNK_SIZE*CHUNK_SIZE tiles in a vertical slice
     */
    public static final int CHUNK_SIZE = 16;
    /**
     * Vertical Axis, how many vertical slices are in a chunk
     */
    //public static final int CHUNK_HEIGHT = 512;

    protected ChunkSection[] sections;

    /*protected byte[][][] tiles;
    protected byte[][][] metadata;*/
    //This is what it saves to a file
    //protected Map<Vector3f, TileData> blocksChanged = new HashMap<>();

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
     * @param z y position of the south face
     */
    public Chunk(float x, float y, float z, int height)
    {
        super();
        this.position = new Vector3f(x,y,z);

        this.sections = new ChunkSection[] {new ChunkSection(0)};
        Logger.debug("Chunk created at: " + position + "; Containing " + CHUNK_SIZE * ChunkSection.SIZE*height * CHUNK_SIZE + "tiles.");

        /*this.tiles = new byte[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];
        this.metadata = new byte[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];*/
        init();
    }

    protected void init()
    {
        float seedVal = 0;//(float)WorldOld.getWorld().SEED / 256f;
        //fill with terrain
        for (int i = 0; i < CHUNK_SIZE; ++i)
        {
            for (int k = 0; k < CHUNK_SIZE; ++k)
            {
//                setTile(Tile.Tiles.tileStoneCliff, i, 0, k);
//                setTile(Tile.Tiles.tileGrass, i, 1, k);
//                setTile(Tile.Tiles.tileGrass, i, 2, k);
//                setTile(Tile.Tiles.tileGrass, i, 3, k);
                //System.out.println((this.getPosX()+i) + "_" + (this.getPosZ()+k));

                float h = CHUNK_SIZE* WorldOld.CHUNK_NUM_XZ - (float)Math.sqrt(
                        (((CHUNK_SIZE* WorldOld.CHUNK_NUM_XZ*0.5f)-(this.getPosX()+i))*((CHUNK_SIZE* WorldOld.CHUNK_NUM_XZ*0.5f)-(this.getPosX()+i)))
                        +(((CHUNK_SIZE* WorldOld.CHUNK_NUM_XZ*0.5f)-(this.getPosZ()+k))*((CHUNK_SIZE* WorldOld.CHUNK_NUM_XZ*0.5f)-(this.getPosZ()+k)))
                );

                //TODO move this to a terrain generator

//                int terrainHeight = (int)(PerlinNoise.perlin(
//                        (seedVal + this.position.x / Tile.TILE_SIZE + i)*Tile.TILE_SIZE*0.025f,
//                        (seedVal + this.position.y / Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT*0.025f,
//                        (seedVal + this.position.y / Tile.TILE_SIZE + k)*Tile.TILE_SIZE*0.025f) //* CHUNK_HEIGHT/4f
//                        /*+
//                        (PerlinNoise.perlin(
//                        (seedVal + this.position.x / Tile.TILE_SIZE + i)*Tile.TILE_SIZE*0.05f,
//                        (seedVal + this.position.y / Tile.TILE_HEIGHT + j)*Tile.TILE_HEIGHT*0.05f,
//                        (seedVal + this.position.y / Tile.TILE_SIZE + k)*Tile.TILE_SIZE*0.05f) * CHUNK_HEIGHT/16f) +
//                        (PerlinNoise.perlin(
//                        (seedVal + this.position.x / Tile.TILE_SIZE + i)*Tile.TILE_SIZE*0.1f,
//                        (seedVal + this.position.y / Tile.TILE_HEIGHT + j)*Tile.TILE_HEIGHT*0.1f,
//                        (seedVal + this.position.y / Tile.TILE_SIZE + k)*Tile.TILE_SIZE*0.1f) * CHUNK_HEIGHT/32f)*/
//                        + h -128);

//                for (int j = (int)this.getPosY(); j < terrainHeight+this.getPosY();// + CHUNK_HEIGHT*0.5f;
//                     ++j)
//                {
//                    //System.out.println("j");
//                    if(j+this.getPosY() < this.getPosY())// || j >= CHUNK_HEIGHT*WorldOld.CHUNK_NUM_Y)
//                    {
//                        continue;
//                    }
//                    setTile(Tile.Tiles.tileStoneCliff, i, j, k);
//                }
                //local coords TODO: move to terrain decorator
                for (int j = sections.length*ChunkSection.SIZE - 1; j >= 0; --j)
                {
                    if(getTile(i,j,k) != 0)
                    {
                        if(j >= WorldOld.SEA_LEVEL+ WorldOld.BEACH_HEIGHT)
                        {
                            setTile(Tile.Tiles.tileGrass, i, j, k);
                            if (WorldOld.getWorld().getRandom().nextBoolean())
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
            }
        }
    }

    public void update(WorldOld world)
    {
        Random random = new Random();
        for (int n = 0; n < WorldOld.chunkTickSpeed; n++)
        {
            int i = random.nextInt(CHUNK_SIZE);
            int k = random.nextInt(CHUNK_SIZE);
            //world, not tilegrid coords;
            for (int j = 0; j < sections.length; j++)
            {
                //TODO: BROKEN
                Tile tile = Tile.Tiles.get(getTile(i,j,k));
                tile.update(null, (int)((this.getPosX()+i)), (int)((this.getPosY()+j)), (int)((this.getPosZ()+k)));
                Tile tile2 = Tile.Tiles.get(getTile(i,j,k));
                if (tile != tile2) flagForReMesh = true;
            }
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

    private boolean isCoordWithinChunk(int x, int z)
    {
        if(x < 0 || x >= CHUNK_SIZE || z < 0 || z >= CHUNK_SIZE)
        {
            return false;
        }
        return true;
    }

    private boolean isCoordWithinChunk(Vector3f pos)
    {
        return isCoordWithinChunk((int)pos.x, (int)pos.z);
    }

    private ChunkSection getSection(int y, boolean createIfMissing)
    {
//
//        if(section == null && createIfMissing)
//        {
//            sections.add(new ChunkSection(y));
//        }
//        return section;
        return null;
    }

    public int getTile(int x, int y, int z)
    {
        if(isCoordWithinChunk(x,z))
        {
            ChunkSection section = getSection(y / ChunkSection.SIZE, false);
            return section != null ? section.getTile(x % ChunkSection.SIZE, y % ChunkSection.SIZE, z % ChunkSection.SIZE) : 0;
        }
        return 0;

        /*
        if(!isCoordWithinChunk(x,y,y))
        {
            //T/ODO replace with a call back to world(?)
            return 0;
        }
        return tiles[x][y][y];*/
    }

    public int getTile(Vector3f pos)
    {
        return getTile((int)pos.x, (int)pos.y, (int)pos.z);
    }

    public <TILE extends Tile> void setTile(TILE tile, int x, int y, int z)
    {
        this.setTile(tile, x,y,z,0);
    }

    public <TILE extends Tile> void setTile(TILE tile, int x, int y, int z, int metadataIn)
    {
        if(isCoordWithinChunk(x,z))
        {
            ChunkSection section = getSection(y / ChunkSection.SIZE, true);
            section.setTile(x % ChunkSection.SIZE, y % ChunkSection.SIZE, z % ChunkSection.SIZE, tile.getIndex(), (byte) metadataIn);
            /*
            tiles[x][y][y] = (byte)(tile.getIndex()%Byte.MAX_VALUE);
            metadata[x][y][y] = (byte)metadataIn;*/
            this.flagForReMesh = true;
        }
    }

    public <TILE extends Tile> void setTile(TILE tile, Vector3f pos)
    {
        this.setTile(tile, (int)pos.x, (int)pos.y, (int)pos.z);
    }

    public <TILE extends Tile> void setTile(TILE tile, Vector3f pos, int metadataIn)
    {
        this.setTile(tile, (int)pos.x, (int)pos.y, (int)pos.z, metadataIn);
    }

    public int getMetadata(int x, int y, int z)
    {
        if(isCoordWithinChunk(x,z))
        {
            ChunkSection section = getSection(y / ChunkSection.SIZE, false);
            return section != null ? section.getMeta(x % ChunkSection.SIZE, y % ChunkSection.SIZE, z % ChunkSection.SIZE) : 0;
            /*return metadata[x][y][y];*/
        }
        return 0;
    }

    public int getMetadata(Vector3f pos)
    {
        return this.getMetadata((int)pos.x, (int)pos.x, (int)pos.z);
    }

    public void setMetadata(int x, int y, int z, int metadataIn)
    {
        //System.out.println(x+","+y+","+y);
        if(isCoordWithinChunk(x,z))
        {
            ChunkSection section = getSection(y / ChunkSection.SIZE, false);
            if(section != null)
            {
                EnumTileShape rType = Tile.Tiles.get(getTile(x,y,z)).getTileShape(getMetadata(x,y,z));
                section.setMeta(x,y,z, (byte) metadataIn);
                if(rType != Tile.Tiles.get(getTile(x,y,z)).getTileShape(metadataIn))
                {
                    this.flagForReMesh = true;
                }
            }
            /*
            //System.out.println();
            //System.out.println(metadata[x][y][y]);
            EnumTileShape rType = Tile.Tiles.get(tiles[x][y][y]).getTileShape(metadata[x][y][y]);
            metadata[x][y][y] = (byte)(metadataIn);
            if(rType != Tile.Tiles.get(tiles[x][y][y]).getTileShape(metadataIn))
            {
                this.flagForReMesh = true;
            }*/
            //System.out.println(metadata[x][y][y]);
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

    public static final ChunkSection empSection = new ChunkSection(-1);

    public boolean isEmpty()
    {

        for (int i = 0; i < sections.length; i++)
        {
            if(getSection(i, false).isEmpty(true));
        }
        return true;
        /*return Arrays.deepEquals(tiles, new byte[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE])
                && Arrays.deepEquals(metadata, new byte[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE]);*/
    }

    public int getHeight()
    {
        return this.sections.length*ChunkSection.SIZE;
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

    protected static class ChunkSection
    {
        private int[] tiles;
        public final int yPos;

        private boolean isEmpty;

        public static final int SIZE = Chunk.CHUNK_SIZE;

        private static final int TILE_MASK = 0xff_ff_ff_00; //16-4294967040
        private static final int META_MASK = 0x00_00_00_ff; //0-15

        public ChunkSection(int yPos)
        {
            this.yPos = yPos;
            tiles = new int[SIZE*SIZE*SIZE];
        }

        /**
         *
         * @param check You aren't going to check when rendering, only when updating
         * @return
         */
        public boolean isEmpty(boolean check)
        {
            if(check)
            {
                for (int i = 0; i < tiles.length; i++)
                {
                    if (!isAir(i)) return isEmpty = false;
                }
                return isEmpty = true;
            }
            return isEmpty;
        }

        public boolean isAir(int i)
        {
            return getTile(i) == Tile.Tiles.tileAir.getIndex();
        }

        public boolean isAir(int x, int y, int z)
        {
            return isAir(getIndex(x,y,z));
        }

        private int getIndex(int x, int y, int z)
        {
            return Math.min(Math.max(0,x+(y*SIZE)+(z*SIZE*SIZE)), tiles.length);
        }

        public int getTile(int i)
        {
            //bitshifts it back
            return (tiles[i] >> 8) & 0x00_ff_ff_ff;
        }

        public int getTile(int x, int y, int z)
        {
            return getTile(getIndex(x,y,z));
        }

        public void setTile(int x, int y, int z, Tile tile, byte metaIn)
        {
            this.setTile(x,y,z, tile.getIndex(), metaIn);
        }

        public void setTile(int x, int y, int z, Tile tile)
        {
            this.setTile(x,y,z, tile.getIndex());
        }

        public void setTile(int x, int y, int z, int tile, byte metaIn)
        {
            //0x00nnnnnn << 8 & TILE_MASK |
            tiles[getIndex(x,y,z)] = (((tile << 8) & TILE_MASK) | (metaIn & META_MASK) );
            //if isEmpty and a nonAir tile is added, it's no longer empty.
            if(isEmpty(false) && (getTile(x,y,z) != Tile.Tiles.tileAir.getIndex())) this.isEmpty = true;
        }

        public void setTile(int x, int y, int z, int tile)
        {
            setTile(x,y,z,tile,(byte) 0);
        }

        public int getMeta(int x, int y, int z)
        {
            return (tiles[getIndex(x,y,z)]) & META_MASK;
        }

        public void setMeta(int x, int y, int z, byte meta)
        {
            tiles[getIndex(x,y,z)] = ((tiles[getIndex(x,y,z)] & TILE_MASK) | (meta & META_MASK));
        }

        public void incrementMeta(int x, int y, int z)
        {
            setMeta(x,y,z, (byte) (getMeta(x,y,z) + 1));
        }

        public void decrementMeta(int x, int y, int z)
        {
            setMeta(x,y,z, (byte) (getMeta(x,y,z) - 1));
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof ChunkSection)) return false;

            ChunkSection section = (ChunkSection) o;

            return Arrays.equals(tiles, section.tiles);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(tiles);
        }

        public int getYPos(ChunkSection chunkSection)
        {
            return this.yPos;
        }
    }
}
