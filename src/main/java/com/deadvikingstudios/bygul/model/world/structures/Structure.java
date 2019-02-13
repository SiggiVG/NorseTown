package com.deadvikingstudios.bygul.model.world.structures;

import com.deadvikingstudios.bygul.controller.GameContainer;
import com.deadvikingstudios.bygul.model.events.StructureEventHandler;
import com.deadvikingstudios.bygul.model.items.ItemStack;
import com.deadvikingstudios.bygul.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.bygul.model.tiles.Tile;
import com.deadvikingstudios.bygul.utils.vector.Vector2i;
import com.deadvikingstudios.bygul.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.deadvikingstudios.bygul.model.world.structures.Chunk.SIZE;

/**
 * A structure is both a blueprint and an object in the world.
 * It does not know it's own location.
 */
public class Structure implements Serializable
{
    protected Vector3i position = new Vector3i(0,0,0);

    protected HashMap<Vector2i, ChunkColumn> chunks = new HashMap<Vector2i, ChunkColumn>();

    private Structure parent;

    private Structure()
    {
        this(null, null, true);
    }

    public Structure(Vector3i pos, Structure parent, boolean doInit)
    {
        if(pos != null)
        {
            this.position = new Vector3i(pos);
        }
//        Can be removed since it begins as instantiated to 0,0,0
//        else
//        {
//            this.position = new Vector3i();
//        }
        this.setParent(parent);

        StructureEventHandler.onStructureCreated(this);

        if(doInit)
        {
            init();
        }

    }

    /**
     * creates a copy of the structure
     * @param structure
     */
    public Structure(Structure structure, Vector3i position)
    {
        this(structure, position, null);
//        this.dockedStructures = copyDocked(structure, this);
    }

    public Structure(Structure structure, Structure parent)
    {
        this(structure, null, parent);
    }

    public Structure(Structure structure, Vector3i position, Structure parent)
    {
        this(position, parent, false);
        this.chunks = copyChunks(structure.chunks, this);
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
    public Chunk getChunkAt(int x, int y, int z)
    {
//        List<Chunk> chunkies = new ArrayList<Chunk>();
        ChunkColumn inChunkCol = this.getChunks().get(new Vector2i(x / Chunk.SIZE, z / Chunk.SIZE));
        if(inChunkCol != null)
        {
            //Chunk inChunk =
            return inChunkCol.getChunk(x, y, z);
//            if (inChunk != null)
//            {
//                if(inChunkCol.getTile(x,y,z) != Tile.NorseTiles.tileAir)
//                {
//                    chunkies.add(inChunk);
//                }
//            }
        }
//        for (Map.Entry<Vector3i, Structure> entry : this.dockedStructures.entrySet())
//        {
//            Structure structure = entry.getValue();
//            //make sure to offset by the position of the structure
//            chunkies.addAll(structure.getChunkAt(x - structure.position.x, y - structure.position.y, z - structure.position.z));
//        }
        return null;
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
        if(tile == null) return false;
        if(tile == this.getTile(x,y,z)) return false;
        if(tile.isAir() && this.getTile(x,y,z).isAir()) return false;

        //TODO: check if a tile exists there in another structure, do this in World?
//        Structure structCheck = World.getCurrentWorld().getStructureAt(x+this.position.x, y+this.position.y, z+this.position.z);
//        if(structCheck != this && structCheck != null) return false;

        int i = Math.floorMod(x, SIZE);
        int k = Math.floorMod(z, SIZE);

        Vector2i pos = new Vector2i(Math.floorDiv(x, SIZE), Math.floorDiv(z, SIZE));

        ChunkColumn col = this.chunks.get(pos);

        if(col == null)
        {
//            Logger.debug(pos);
//            Logger.debug(pos.hashCode());
            col = new ChunkColumn(this,pos);
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
//        Tile tileAt = Tile.NorseTiles.tileAir;

        //check this structure's chunk
        int i = Math.floorMod(x, SIZE);
        int k = Math.floorMod(z, SIZE);

        ChunkColumn col = chunks.get( new Vector2i(Math.floorDiv(x, SIZE), Math.floorDiv(z, SIZE)));
        if(col != null)
        {
            return col.getTile(i,y,k);
//            tileAt = col.getTile(i, y, k);
//            if(tileAt != Tile.NorseTiles.tileAir) return tileAt;
        }

//        if(this.canHaveChildren() && !this.dockedStructures.isEmpty())
//        {
//            for (Map.Entry<Vector3i, Structure> struct : this.dockedStructures.entrySet())
//            {
//                //it's never getting in here
//                Vector3i sPos = struct.getValue().getPosition();
//                Tile tileAt2 = struct.getValue().getTile(x-sPos.x,y-sPos.y,z-sPos.z);
//                tileAt = (tileAt2 != Tile.NorseTiles.tileAir) ? tileAt2 : tileAt;
//            }
//        }
        return Tile.Tiles.tileAir;
    }

    public Structure getParent()
    {
        return parent;
    }

    public boolean setParent(Structure parent)
    {
        //parent is already null, no change
        if(this.parent == null && parent == null) return true;
        //parent is already parent, no change
        else if(this.parent == parent) return true;
        //can't be it's own parent
        else if(parent == this) return false;
        //sets parent to null
        else if(parent == null)
        {
            //updates the position to be absolute
            Vector3i pPos = this.parent.getPosition();
            this.position = new Vector3i(pPos.x+this.position.x, pPos.y+this.position.y, pPos.z+this.position.z);
            this.parent = null;
            return true;
        }
        //parent isnt permitted to have children
        else if(!parent.canHaveChildren()) return false;
        //ensure that it's not creating a loop
        Structure p = parent;
        while(p.parent != null)
        {
            if(p.parent == this) return false;
            p = p.parent;
        }
        //setting parent
        this.parent = parent;
        this.setPosition(this.position);
        return true;
    }

//    private boolean containsStructure(Structure structure)
//    {
//        if(!this.canHaveChildren()) return false;
//        if(this.dockedStructures.containsValue(structure)) return true;
//        for (Map.Entry<Vector3i, Structure> struct : this.dockedStructures.entrySet())
//        {
//            if(struct.getValue().containsStructure(structure)) return true;
//        }
//        return false;
//    }

    public boolean isParentOf(Structure structure)
    {
        return structure.parent == this;
    }

    public boolean isChildOf(Structure structure)
    {
        return this.parent == structure;
    }

    public boolean isDescendentOf(Structure structure)
    {
        Structure p = this;
        while(p.parent != null)
        {
            if(p.parent == structure) return true;
            p = p.parent;
        }
        return false;
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
        //TODO: update each chunk column
//        for (Map.Entry<Vector3i, Structure> entry : this.dockedStructures.entrySet() )
//        {
//            entry.getValue().update();
//        }
    }

//    public <STRUCTURE extends Structure> void addDockedStructure(STRUCTURE structure)
//    {
//        if(!this.canHaveChildren()) return;
//        this.dockedStructures.put(structure.position, structure);
//    }

//    public <STRUCTURE extends Structure> void removeDockedStructure(STRUCTURE structure)
//    {
//        this.dockedStructures.remove(structure.position);
//    }

    public Vector3i getPosition()
    {
        //is parentless, so it's position is absolute
        if(this.parent == null)return position;
        //has a parent, so it's position is relative to it's parent's.
        Vector3i pPos = this.parent.getPosition();
        return new Vector3i(pPos.x+this.position.x, pPos.y+this.position.y, pPos.z+this.position.z);
    }

    public void setPosition(Vector3i position)
    {
        //is parentless, so it's position is absolute
        if(this.parent == null) this.position = position;
        //has a parent, so it's position is relative to it's parent's.
        else
        {
            Vector3i pPos = this.parent.getPosition();
            this.position = new Vector3i(position.x-pPos.x, position.y-pPos.y, position.z-pPos.z);
        }
    }

    public List<ItemStack> destroy(Structure parent, boolean dropItems)
    {
        for (Map.Entry<Vector2i, ChunkColumn> entry : this.getChunks().entrySet())
        {
            entry.getValue().getChunks().clear();
        }
        this.getChunks().clear();

        StructureEventHandler.onStructureDestroyed(this);

        return null;
    }

    public boolean containsPoint(Vector3f point)
    {
        //if the chunk at the location offset by this structure's origin is not null
        Vector3i pos = this.getPosition();
        Chunk chunk = this.getChunkAt((int) point.x - pos.x, (int) point.y - pos.y, (int) point.z - pos.z);
        return chunk != null;
    }

    /**
     *
     * @return a list of AABBs describing the chunks held within that are offset be the position of the structure
     */
    public List<AxisAlignedBoundingBox> getRoughCollider()//boolean includeDocked)
    {
        List<AxisAlignedBoundingBox> list = new ArrayList<AxisAlignedBoundingBox>();
        for (Map.Entry<Vector2i, ChunkColumn> entry : this.chunks.entrySet())
        {
            list.addAll(entry.getValue().getAxisAlignedBoundingBox());
        }
//        if(includeDocked && this.canHaveChildren())
//        {
//            for (Map.Entry<Vector3i, Structure> entry : this.dockedStructures.entrySet())
//            {
//                list.addAll(entry.getValue().getRoughCollider(true));
//            }
//        }
        return list;
    }

    public static <STRUCTURE extends Structure> STRUCTURE copy(STRUCTURE structureIn, Structure parent)//, boolean copyDocked)
    {
        Structure copy = null;
        try
        {
            copy = structureIn.getClass().getConstructor(Vector3i.class, Structure.class, boolean.class).newInstance(
                    new Vector3i(structureIn.position), parent,false);

            copy.chunks = copyChunks(structureIn.chunks, copy);
            copy.parent = parent;

//            if(copyDocked && copy.canHaveChildren())
//            {
//                copy.dockedStructures = copyDocked(structureIn, parent);
//            }

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

//    private static <STRUCTURE extends Structure> HashMap<Vector3i, Structure> copyDocked(STRUCTURE structureIn, Structure parent)
//    {
//        if(!structureIn.canHaveChildren()) return null;
//        HashMap<Vector3i, Structure> copyDocked = new HashMap<Vector3i, Structure>();
//
//        for (Map.Entry<Vector3i, Structure> entry : structureIn.dockedStructures.entrySet() )
//        {
//            copyDocked.put(new Vector3i(entry.getKey()), copy(structureIn, parent,true));
//        }
//
//        return copyDocked;
//    }
}
