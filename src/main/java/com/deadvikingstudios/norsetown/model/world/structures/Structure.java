package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.model.items.ItemStack;
import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector2i;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

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
    protected Vector3i position = new Vector3i(0,0,0);

    protected HashMap<Vector2i, ChunkColumn> chunks;

    protected Structure parent;

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
        this(null, null, true);
    }

    public Structure(Vector3i pos, Structure parent, boolean doInit)
    {
        if(pos != null)
        {
            this.position = pos;
        }
        this.parent = parent;
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
        this.dockedStructures = copyDocked(structure, this);
    }

    public static <STRUCTURE extends Structure> STRUCTURE copy(STRUCTURE structureIn, Structure parent, boolean copyDocked)
    {
        Structure copy = null;
        try
        {
            copy = structureIn.getClass().getConstructor(Vector3i.class, Structure.class, boolean.class).newInstance(new Vector3i(structureIn.position), parent,false);

            copy.chunks = copyChunks(structureIn.chunks, copy);

            if(copyDocked)
            {
                copy.dockedStructures = copyDocked(structureIn, parent);
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

    public static <STRUCTURE extends Structure> HashMap<Vector3i, Structure> copyDocked(STRUCTURE structureIn, Structure parent)
    {
        HashMap<Vector3i, Structure> copyDocked = new HashMap<Vector3i, Structure>();

        for (Map.Entry<Vector3i, Structure> entry : structureIn.dockedStructures.entrySet() )
        {
            copyDocked.put(new Vector3i(entry.getKey()), copy(structureIn, parent,true));
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
//            Logger.debug(pos);
//            Logger.debug(pos.hashCode());
            col = new ChunkColumn(pos);
            col.setStructure(this);
            this.chunks.put(pos, col);
        }

        col.setTile(tile, i, y, k);

        //if the column is now empty, remove it.
        if(tile.isAir())
        {
            if(col.isEmpty())
            {
                chunks.remove(col.position);
                GameContainer.removeStructureMesh(col);
            }
        }
    }

    public Tile getTile(Vector3f pos)
    {
        return this.getTile((int)pos.x, (int)pos.y, (int)pos.z);
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

    public Structure getParent()
    {
        return parent;
    }

    public void setParent(Structure parent)
    {
        assert parent != null;
        this.parent = parent;
        parent.addDockedStructure(this);
    }

    //    private boolean chunkExists(Vector3i pos)
//    {
//        if(chunks.containsKey(pos.toVector2i()))
//        {
//            return chunks.get(pos.toVector2i()).chunkExists(pos.y);
//        }
//        return false;
//    }

//    private Chunk getChunkAt(int x, int y, int y, boolean createIfDoesntExist)
//    {
//        Vector3i pos = new Vector3i(x / SIZE,y / SIZE, y / SIZE);
//        if(!chunks.containsKey(pos.toVector2i()))
//        {
//            if(createIfDoesntExist)
//            {
//                ChunkColumn col = new ChunkColumn(pos.toVector2i());
//                this.chunks.put(pos.toVector2i(), col);
//                return col.getChunk(x, y, y, createIfDoesntExist);
//            }
//        }
//        else
//        {
//            ChunkColumn col = chunks.get(pos.toVector2i());
//            if(!col.chunkExists(pos.y))
//            {
//                return col.getChunk(x,y,y,createIfDoesntExist);
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

    public <STRUCTURE extends Structure> void addDockedStructure(STRUCTURE structure)
    {
        this.dockedStructures.put(structure.position, structure);
    }

    public <STRUCTURE extends Structure> void removeDockedStructure(STRUCTURE structure)
    {
        this.dockedStructures.remove(structure.position);
    }

    public Vector3i getPosition()
    {
        return position;
    }

    public void setPosition(Vector3i position)
    {
        this.position = position;
    }

    public List<AxisAlignedBoundingBox> getRoughCollider(boolean includeChildren)
    {
        List<AxisAlignedBoundingBox> aabbs = new ArrayList<AxisAlignedBoundingBox>();
        for (Map.Entry<Vector2i, ChunkColumn> cols : this.chunks.entrySet())
        {
            aabbs.addAll(cols.getValue().getRoughCollider());
        }
        if(includeChildren)
        {
            for (Map.Entry<Vector3i, Structure> child : this.dockedStructures.entrySet())
            {
                aabbs.addAll(child.getValue().getRoughCollider(true));
            }
        }
        return aabbs;
    }

    public List<ItemStack> destroy(Structure parent, boolean dropItems)
    {
        for (Map.Entry<Vector2i, ChunkColumn> entry : this.getChunks().entrySet())
        {
            entry.getValue().getChunks().clear();
            entry.getValue().flagForReMesh();
        }
        this.getChunks().clear();
        if(parent != null)
        {
            parent.removeDockedStructure(this);
        }

        return null;
    }
}
