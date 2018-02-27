package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.model.events.TileEventHandler;
import com.deadvikingstudios.norsetown.model.items.ItemStack;
import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.World;
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
        else
        {
            pos = new Vector3i();
        }
        this.parent = parent;
        chunks = new HashMap<Vector2i, ChunkColumn>();
        dockedStructures = new HashMap<Vector3i, Structure>();
        if(doInit)
        {
            init();
        }
    }

    public Structure(Structure structure)
    {
        this.chunks = copyChunks(structure.chunks, this);
        this.dockedStructures = copyDocked(structure, this);
    }

    public void init(){}

    public HashMap<Vector2i, ChunkColumn> getChunks()
    {
        return this.chunks;
    }

    public boolean canHaveChildren()
    {
        return false;
    }

    /**
     * Input tile-in-structure coordinates
     * @param x
     * @param y
     * @param z
     * @return
     */
    public List<Chunk> getChunkAt(int x, int y, int z)
    {
        List<Chunk> chunkies = new ArrayList<Chunk>();
        ChunkColumn inChunkCol = this.getChunks().get(new Vector2i(x / Chunk.SIZE, z / Chunk.SIZE));
        if(inChunkCol != null)
        {
            Chunk inChunk = inChunkCol.getChunk(x, y, z);
            if (inChunk != null)
            {
                if(inChunkCol.getTile(x,y,z) != Tile.Tiles.tileAir)
                {
                    chunkies.add(inChunk);
                }
            }
        }
        for (Map.Entry<Vector3i, Structure> entry : this.dockedStructures.entrySet())
        {
            Structure structure = entry.getValue();
            //make sure to offset by the position of the structure
            chunkies.addAll(structure.getChunkAt(x - structure.position.x, y - structure.position.y, z - structure.position.z));
        }
        return chunkies;
    }

    public boolean setTile(Tile tile, int x, int y, int z)
    {
        return this.setTile(tile, x, y, z, 0, false);
    }

    public boolean setTile(Tile tile, int x, int y, int z, int metadata)
    {
        return this.setTile(tile, x, y, z, metadata, false);
    }

    public boolean setTile(Tile tile, int x, int y, int z, int metadata, boolean byPlayer)
    {
        if(tile == this.getTile(x,y,z)) return false;
        if(tile.isAir() && this.getTile(x,y,z).isAir()) return false;

        //TODO: check if a tile exists there in another structure
        Structure structCheck = World.getCurrentWorld().getStructureAt(x+this.position.x, y+this.position.y, z+this.position.z);
        if(structCheck != this && structCheck != null) return false;

        int i = Math.floorMod(x, SIZE);
        int k = Math.floorMod(z, SIZE);

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

        col.setTile(tile, i, y, k, metadata, byPlayer);


        //if the column is now empty, remove it.
        if(tile.isAir())
        {
            if(col.isEmpty())
            {
                chunks.remove(col.position);
                GameContainer.removeStructureMesh(col);
            }
        }
        return true;
    }

    public Tile getTile(Vector3f pos)
    {
        return this.getTile((int)pos.x, (int)pos.y, (int)pos.z);
    }

    //TODO: check children?
    public Tile getTile(int x, int y, int z)
    {
        Tile tileAt = Tile.Tiles.tileAir;

        //check this structure's chunk
        int i = Math.floorMod(x, SIZE);
        int k = Math.floorMod(z, SIZE);

        ChunkColumn col = chunks.get( new Vector2i(Math.floorDiv(x, SIZE), Math.floorDiv(z, SIZE)));
        if(col != null)
        {
            tileAt = col.getTile(i, y, k);
            if(tileAt != Tile.Tiles.tileAir) return tileAt;
        }

        if(this.canHaveChildren() && !this.dockedStructures.isEmpty())
        {
            for (Map.Entry<Vector3i, Structure> struct : this.dockedStructures.entrySet())
            {
                //it's never getting in here
                Vector3i sPos = struct.getValue().getPosition();
                Tile tileAt2 = struct.getValue().getTile(x-sPos.x,y-sPos.y,z-sPos.z);
                tileAt = (tileAt2 != Tile.Tiles.tileAir) ? tileAt2 : tileAt;
            }
        }
        return tileAt;
    }

    public Structure getParent()
    {
        return parent;
    }

    public void setParent(Structure parent)
    {
        if(!parent.canHaveChildren()) return;
        //TODO: check that parent is not a child of this and that this is not a parent of parent
        if(this.containsStructure(parent) || parent.isChildOf(this)) return; //TODO: Error

        if(this.parent != null) this.parent.removeDockedStructure(this);
        this.parent = parent;
        parent.addDockedStructure(this);
    }

    private boolean containsStructure(Structure structure)
    {
        if(!this.canHaveChildren()) return false;
        if(this.dockedStructures.containsValue(structure)) return true;
        for (Map.Entry<Vector3i, Structure> struct : this.dockedStructures.entrySet())
        {
            if(struct.getValue().containsStructure(structure)) return true;
        }
        return false;
    }

    private boolean isChildOf(Structure structure)
    {
        return parent != null && parent.isChildOf(this);
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
        if(!this.canHaveChildren()) return;
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

    public boolean containsPoint(Vector3f point)
    {
        //if the chunk at the location offset by this structure's origin is not null
        if(!this.getChunkAt((int)point.x-this.position.x, (int)point.y-this.position.y, (int)point.z-this.position.z).isEmpty())
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @return a list of AABBs describing the chunks held within that are offset be the position of the structure
     */
    public List<AxisAlignedBoundingBox> getRoughCollider(boolean includeDocked)
    {
        List<AxisAlignedBoundingBox> list = new ArrayList<AxisAlignedBoundingBox>();
        for (Map.Entry<Vector2i, ChunkColumn> entry : this.chunks.entrySet())
        {
            list.addAll(entry.getValue().getAxisAlignedBoundingBox());
        }
        if(includeDocked && this.canHaveChildren())
        {
            for (Map.Entry<Vector3i, Structure> entry : this.dockedStructures.entrySet())
            {
                list.addAll(entry.getValue().getRoughCollider(true));
            }
        }
        return list;
    }

    public static <STRUCTURE extends Structure> STRUCTURE copy(STRUCTURE structureIn, Structure parent, boolean copyDocked)
    {
        Structure copy = null;
        try
        {
            copy = structureIn.getClass().getConstructor(Vector3i.class, Structure.class, boolean.class).newInstance(
                    new Vector3i(structureIn.position), parent,false);

            copy.chunks = copyChunks(structureIn.chunks, copy);

            if(copyDocked && copy.canHaveChildren())
            {
                copy.dockedStructures = copyDocked(structureIn, parent);
            }

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }


        return (STRUCTURE) copy;
    }

    private static <STRUCTURE extends Structure> HashMap<Vector2i, ChunkColumn> copyChunks(HashMap<Vector2i, ChunkColumn> chunksIn, STRUCTURE structureOut)
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

    private static <STRUCTURE extends Structure> HashMap<Vector3i, Structure> copyDocked(STRUCTURE structureIn, Structure parent)
    {
        if(!structureIn.canHaveChildren()) return null;
        HashMap<Vector3i, Structure> copyDocked = new HashMap<Vector3i, Structure>();

        for (Map.Entry<Vector3i, Structure> entry : structureIn.dockedStructures.entrySet() )
        {
            copyDocked.put(new Vector3i(entry.getKey()), copy(structureIn, parent,true));
        }

        return copyDocked;
    }
}
