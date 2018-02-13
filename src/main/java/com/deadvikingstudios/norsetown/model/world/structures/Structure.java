package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.ArrayUtils;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.Position3i;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * A structure is both a blueprint and an object in the world.
 * It does not know it's own location.
 */
public class Structure implements Serializable
{
    protected HashMap<Position3i, StructureChunk> chunks;

    protected HashMap<Position3i, Structure> dockedStructures;

    /*
    TODO: construct a way to hasten collision detection by having a preliminary check for where there are any blocks
     */

    public HashMap<Position3i, StructureChunk> getChunks()
    {
        return this.chunks;
    }

    protected boolean flagForReMesh = false;

    public Structure()
    {
        this(true);
    }

    public Structure(boolean doInit)
    {
        chunks = new HashMap<Position3i, StructureChunk>();
        dockedStructures = new HashMap<Position3i, Structure>();
        if(doInit)
        {
            init();
        }
    }

    public <STRUCTURE extends Structure> Structure(STRUCTURE structure)
    {

    }

    public static <STRUCTURE extends Structure> STRUCTURE copy(STRUCTURE structure, boolean copyDocked)
    {
        Structure copy = null;
        try
        {
            copy = structure.getClass().getConstructor(boolean.class).newInstance(false);
            copy.chunks = copyChunks(structure);

            if(copyDocked)
            {
                copy.dockedStructures = copyDocked(structure);
            }

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }


        return (STRUCTURE) copy;
    }

    public static <STRUCTURE extends Structure> HashMap<Position3i, StructureChunk> copyChunks(STRUCTURE structure)
    {
        HashMap<Position3i, StructureChunk> copyChunks = new HashMap<Position3i, StructureChunk>();

        for (Map.Entry<Position3i, StructureChunk> entry : structure.chunks.entrySet() )
        {
            copyChunks.put(new Position3i(entry.getKey()), new StructureChunk(entry.getValue()));
        }

        return copyChunks;
    }

    public static <STRUCTURE extends Structure> HashMap<Position3i, Structure> copyDocked(STRUCTURE structure)
    {
        HashMap<Position3i, Structure> copyDocked = new HashMap<Position3i, Structure>();

        for (Map.Entry<Position3i, Structure> entry : structure.dockedStructures.entrySet() )
        {
            copyDocked.put(new Position3i(entry.getKey()), copy(structure, true));
        }

        return copyDocked;
    }

    public void init(){}

    public boolean isFlagForReMesh()
    {
        return flagForReMesh;
    }

    public void setTile(Tile tile, int x, int y, int z, int meta)
    {
        if(tile == this.getTile(x,y,z)) return;
        if(tile.isAir() && this.getTile(x,y,z).isAir()) return;

        int i = x % StructureChunk.SIZE;
        int j = y % StructureChunk.SIZE;
        int k = z % StructureChunk.SIZE;
        //        i = 16                  -  0:15
        if(x < 0)
        {
            i = StructureChunk.SIZE + i - 1;
            x -=StructureChunk.SIZE;
        }
        if(y < 0)
        {
            j = StructureChunk.SIZE + j - 1;
            y-=StructureChunk.SIZE;
        }
        if(z < 0)
        {
            k = StructureChunk.SIZE + k - 1;
            z-=StructureChunk.SIZE;
        }

        StructureChunk chunk = getChunkAt(x,y,z);

        chunk.setTile(tile, i,j,k, (byte) meta);

        if(tile.isAir())
        {
            if(chunk.isEmpty())
            {
                chunks.remove(chunk.position);
            }
        }
        this.flagForReMesh = true;
    }

    public Tile getTile(int x, int y, int z)
    {
        int i = x % StructureChunk.SIZE;
        int j = y % StructureChunk.SIZE;
        int k = z % StructureChunk.SIZE;
        if(x < 0)
        {
            i = StructureChunk.SIZE + i - 1;
            x-=StructureChunk.SIZE;
        }
        if(y < 0)
        {
            j = StructureChunk.SIZE + j - 1;
            y-=StructureChunk.SIZE;
        }
        if(z < 0)
        {
            k = StructureChunk.SIZE + k - 1;
            z-=StructureChunk.SIZE;
        }

        Position3i chunkPos = new Position3i(x / StructureChunk.SIZE, y / StructureChunk.SIZE, z / StructureChunk.SIZE);
        if(chunkExists(chunkPos))
        {
            return getChunkAt(x,y,z).getTile(i,j,k);
        }
        else return Tile.Tiles.tileAir;
    }

    private boolean chunkExists(Position3i pos)
    {
        return chunks.containsKey(pos);
    }

    private StructureChunk getChunkAt(int x, int y, int z)
    {
        Position3i pos = new Position3i(x / StructureChunk.SIZE,y / StructureChunk.SIZE, z / StructureChunk.SIZE);
        if(!chunkExists(pos))
        {
            StructureChunk chunk = new StructureChunk(pos);
            chunks.put(chunk.position, chunk);
        }
        return chunks.get(pos);

    }

    public void setFlagForReMesh(boolean flagForReMesh)
    {
        this.flagForReMesh = flagForReMesh;
    }

    public void update()
    {
        for (Map.Entry<Position3i, Structure> entry : this.dockedStructures.entrySet() )
        {
            entry.getValue().update();
        }
    }

    public static class StructureChunk
    {
        public final Position3i position;

        public final static byte SIZE = 16;
        public final static int SIZE_CUBED = SIZE*SIZE*SIZE;

        private int[][][] tiles;

        StructureChunk(StructureChunk chunk)
        {
            this.position = new Position3i(chunk.position);
            this.tiles = ArrayUtils.deepCopyOf(chunk.tiles);
        }

        StructureChunk(Position3i position)
        {
            this.position = new Position3i(position);
            this.tiles = new int[SIZE][SIZE][SIZE];
            Logger.debug("Structure Chunk created at " + position.x + "," + position.y + "," + position.z);
        }

        private boolean withinBounds(byte x, byte y, byte z)
        {
            return (x >= 0 && x < SIZE && y >= 0 && y < SIZE && z >= 0 && z < SIZE);
        }

        Tile getTile(int x, int y, int z)
        {
            if(withinBounds((byte)x,(byte)y,(byte)z))return Tile.Tiles.get(tiles[x][y][z]);
            return Tile.Tiles.tileAir;
        }

        void setTile(Tile tile, int x, int y, int z, int meta)
        {
            if(withinBounds((byte)x,(byte)y,(byte)z))
            {
                this.tiles[x][y][z] = tile.getIndex();
            }

        }

        boolean isEmpty()
        {
            for (byte i = 0; i < tiles.length; i++)
            {
                for (byte j = 0; j < tiles[i].length; j++)
                {
                    for (byte k = 0; k < tiles[i][j].length; k++)
                    {
                        if(!getTile(i,j,k).isAir())
                        {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
}
