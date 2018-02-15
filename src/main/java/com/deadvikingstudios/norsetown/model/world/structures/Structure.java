package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.Vector2i;
import com.deadvikingstudios.norsetown.utils.Vector3i;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.deadvikingstudios.norsetown.model.world.structures.Chunk.SIZE;

/**
 * A structure is both a blueprint and an object in the world.
 * It does not know it's own location.
 */
public class Structure implements Serializable
{
    protected HashMap<Vector2i, ChunkColumn> chunks;

    /**
     * Might change this to be a List? accessing structures by location is useful, but not super so.
     */
    protected HashMap<Vector3i, Structure> dockedStructures;

    /*
    TODO:
    construct a way to hasten collision detection by having a preliminary check for where there are any blocks
    center of mass calculator/setter for use with entities.
     */

    public HashMap<Vector2i, ChunkColumn> getChunks()
    {
        return this.chunks;
    }

    public Structure()
    {
        this(true);
    }

    public Structure(boolean doInit)
    {
        chunks = new HashMap<Vector2i, ChunkColumn>();
        dockedStructures = new HashMap<Vector3i, Structure>();
        if(doInit)
        {
            init();
        }
    }

    public <STRUCTURE extends Structure> Structure(STRUCTURE structure)
    {
        this.chunks = copyChunks(structure.chunks, this);
        this.dockedStructures = copyDocked(structure);
    }

    public static <STRUCTURE extends Structure> STRUCTURE copy(STRUCTURE structureIn, boolean copyDocked)
    {
        Structure copy = null;
        try
        {
            copy = structureIn.getClass().getConstructor(boolean.class).newInstance(false);
            copy.chunks = copyChunks(structureIn.chunks, copy);

            if(copyDocked)
            {
                copy.dockedStructures = copyDocked(structureIn);
            }

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }


        return (STRUCTURE) copy;
    }

    public static <STRUCTURE extends Structure> HashMap<Vector2i, ChunkColumn> copyChunks(HashMap<Vector2i, ChunkColumn> chunksIn, Structure structureOut)
    {
        HashMap<Vector2i, ChunkColumn> copyChunks = new HashMap<Vector2i, ChunkColumn>();

        for (Map.Entry<Vector2i, ChunkColumn> entry : chunksIn.entrySet() )
        {
            ChunkColumn chunkCol = new ChunkColumn(entry.getValue());
            chunkCol.setStructure(structureOut);
            copyChunks.put(new Vector2i(entry.getKey()), chunkCol);
        }

        return copyChunks;
    }

    public static <STRUCTURE extends Structure> HashMap<Vector3i, Structure> copyDocked(STRUCTURE structureIn)
    {
        HashMap<Vector3i, Structure> copyDocked = new HashMap<Vector3i, Structure>();

        for (Map.Entry<Vector3i, Structure> entry : structureIn.dockedStructures.entrySet() )
        {
            copyDocked.put(new Vector3i(entry.getKey()), copy(structureIn, true));
        }

        return copyDocked;
    }

    public void init(){}

    public void setTile(Tile tile, int x, int y, int z)
    {
        if(tile == this.getTile(x,y,z)) return;
        if(tile.isAir() && this.getTile(x,y,z).isAir()) return;

        int i = Math.floorMod(x, SIZE);
        int k = Math.floorMod(z, SIZE);

        int n = 0;
        if(x < 0) n = -1;
        int m = 0;
        if(z < 0) m = -1;

        Vector2i pos = new Vector2i(Math.floorDiv(x, SIZE), Math.floorDiv(z, SIZE));

        ChunkColumn col = this.chunks.get(pos);

        if(col == null)
        {
            Logger.debug(pos);
            Logger.debug(pos.hashCode());
            col = new ChunkColumn(pos);
            col.setStructure(this);
            this.chunks.put(pos, col);
        }

        col.setTile(tile, i, y, k);

//        if(tile.isAir())
//        {
//            if(chunk.isEmpty())
//            {
//                //remove the mesh for it too
//                chunks.remove(chunk.position);
//            }
//        }
    }

    public Tile getTile(int x, int y, int z)
    {
        int i = Math.floorMod(x, SIZE);
        int k = Math.floorMod(z, SIZE);

        int n = 0;
        if(x < 0) n = -1;
        int m = 0;
        if(z < 0) m = -1;

        ChunkColumn col = chunks.get( new Vector2i(Math.floorDiv(x, SIZE), Math.floorDiv(z, SIZE)));
        if(col != null)
        {
            return col.getTile(i, y, k);
        }
        return Tile.Tiles.tileAir;
    }

//    private boolean chunkExists(Vector3i pos)
//    {
//        if(chunks.containsKey(pos.toVector2i()))
//        {
//            return chunks.get(pos.toVector2i()).chunkExists(pos.y);
//        }
//        return false;
//    }

//    private Chunk getChunkAt(int x, int y, int z, boolean createIfDoesntExist)
//    {
//        Vector3i pos = new Vector3i(x / SIZE,y / SIZE, z / SIZE);
//        if(!chunks.containsKey(pos.toVector2i()))
//        {
//            if(createIfDoesntExist)
//            {
//                ChunkColumn col = new ChunkColumn(pos.toVector2i());
//                this.chunks.put(pos.toVector2i(), col);
//                return col.getChunk(x, y, z, createIfDoesntExist);
//            }
//        }
//        else
//        {
//            ChunkColumn col = chunks.get(pos.toVector2i());
//            if(!col.chunkExists(pos.y))
//            {
//                return col.getChunk(x,y,z,createIfDoesntExist);
//            }
//        }
//
//        return null;
//
//    }

    public void update()
    {
        for (Map.Entry<Vector3i, Structure> entry : this.dockedStructures.entrySet() )
        {
            entry.getValue().update();
        }
    }
}
