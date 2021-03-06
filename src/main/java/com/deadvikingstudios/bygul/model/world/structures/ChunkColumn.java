package com.deadvikingstudios.bygul.model.world.structures;

import com.deadvikingstudios.bygul.model.entities.Entity;
import com.deadvikingstudios.bygul.model.events.StructureEventHandler;
import com.deadvikingstudios.bygul.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.bygul.model.tiles.Tile;
import com.deadvikingstudios.bygul.utils.vector.Vector2i;
import com.deadvikingstudios.bygul.utils.vector.Vector3i;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.deadvikingstudios.bygul.model.world.structures.Chunk.SIZE;

/**
 * A wrapper class that contains Chunks in a vertical column.
 * Also acts as the part of a structure that a mesh is generated for
 *
 * Maybe put a vertical limit on it, and stack them on top of each other?
 * How many vertices is the player going to generate in one building?
 */
public class ChunkColumn extends Entity implements Serializable
{
    private HashMap<Integer, Chunk> chunks;
    private HashMap<Integer, boolean[][][]> colliders;

    public final Vector2i position;

    private boolean flagForReMesh = false;

    private Structure structure;

    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure)
    {
        this.structure = structure;
    }

    public ChunkColumn(Structure structure, Vector2i pos)
    {
        chunks = new HashMap<Integer, Chunk>();
        position = new Vector2i(pos);
        this.setStructure(structure);
        StructureEventHandler.onChunkColumnCreated(this.getStructure(),this);    }

    public ChunkColumn(ChunkColumn col)
    {
        chunks = new HashMap<Integer, Chunk>();
        this.position = new Vector2i(col.position);
        for (Map.Entry<Integer, Chunk> entry : this.chunks.entrySet())
        {
            chunks.put(new Integer(entry.getKey()), new Chunk(entry.getValue()));
        }
        this.setStructure(structure);
        StructureEventHandler.onChunkColumnCreated(this.getStructure(),this);
    }

    public Chunk getChunk(int x, int y, int z)
    {
        return this.getChunk(x,y,z,false);
    }

    public HashMap<Integer, Chunk> getChunks()
    {
        return chunks;
    }

    private Chunk getChunk(int x, int y, int z, boolean createIfDoesntExist)
    {
        if(!chunkExists(Math.floorDiv(y,SIZE)))
        {
            if(createIfDoesntExist)
            {
                Chunk chunk = new Chunk(new Vector3i(Math.floorDiv(x, SIZE), Math.floorDiv(y, SIZE), Math.floorDiv(z, SIZE)));
                chunks.put(Math.floorDiv(y, SIZE), chunk);
            }
        }
        return chunks.get(Math.floorDiv(y, SIZE));
    }

    private boolean chunkExists(int y)
    {
        return chunks.get(y) != null;
    }

    public Tile getTile(int x, int y, int z)
    {
        Chunk chunk = getChunk(x,y,z,false);
        if(chunk != null)
        {
            return chunk.getTile(Math.floorMod(x, SIZE), Math.floorMod(y, SIZE), Math.floorMod(z, SIZE));
        }
        return Tile.Tiles.tileAir;
    }

    public void setTile(Tile tile, int x, int y, int z)
    {
        this.setTile(tile, x, y, z, 0, false);
    }

    public void setTile(Tile tile, int x, int y, int z, int metadata)
    {
        this.setTile(tile, x, y, z, metadata, false);
    }

    public void setTile(Tile tile, int x, int y, int z, int metadata, boolean byPlayer)
    {
        Chunk chunk = getChunk(x,y,z,true);

        //Checks if the two tiles are the same
        if(chunk.getTile(Math.floorMod(x, SIZE), Math.floorMod(y, SIZE), Math.floorMod(z, SIZE)) == tile) return;

        //calls the Chunk's setTile function upon an adjusted coordinate.
        chunk.setTile(tile, Math.floorMod(x, SIZE), Math.floorMod(y, SIZE), Math.floorMod(z, SIZE), (byte) (metadata % 0x8F), byPlayer);

        //If the chunk is now empty, remove it.
        if(tile == Tile.Tiles.tileAir)
        {
            if(chunk.isEmpty())
            {
                this.chunks.remove(y);
            }
        }

        //Alerts the engine that a new mesh needs to be generated. TODO: move this elsewhere
        this.flagForReMesh = true;
    }

    public int getMetadata(int x, int y, int z)
    {
        Chunk chunk = getChunk(x,y,z,false);
        if(chunk != null)
        {
            return chunk.getMetadata(Math.floorMod(x, SIZE), Math.floorMod(y, SIZE), Math.floorMod(z, SIZE));
        }
        return 0;
    }

    public void setMetadata(int x, int y, int z, byte metadata)
    {
        Chunk chunk = getChunk(x,y,z,false);
        if(chunk ==  null) return;

        byte oldMeta = (byte) chunk.getMetadata(Math.floorMod(x, SIZE), Math.floorMod(y, SIZE), Math.floorMod(z, SIZE));
        if(oldMeta == metadata) return;

        Tile tile = chunk.getTile(Math.floorMod(x, SIZE), Math.floorMod(y, SIZE), Math.floorMod(z, SIZE));
        if(tile.isAir()) return;

        chunk.setMetadata(Math.floorMod(x, SIZE), Math.floorMod(y, SIZE), Math.floorMod(z, SIZE), metadata);

//        if(tile.getTileMesh(oldMeta, (int)(x+this.getPosX()*Chunk.SIZE), (int)(y+this.getPosY()*Chunk.SIZE), (int)(x+this.getPosY()*Chunk.SIZE))
//                != tile.getTileMesh(metadata, (int)(x+this.getPosX()*Chunk.SIZE), (int)(y+this.getPosY()*Chunk.SIZE), (int)(x+this.getPosY()*Chunk.SIZE)))
//            this.flagForReMesh();

    }

    @Override
    public void update()
    {

    }

    @Override
    public List<AxisAlignedBoundingBox> getAxisAlignedBoundingBox()
    {
        List<AxisAlignedBoundingBox> aabbs = new ArrayList<AxisAlignedBoundingBox>();
        for (Map.Entry<Integer,Chunk> entry : chunks.entrySet())
        {
            Vector3i chunkPos = entry.getValue().position;
            aabbs.add(new AxisAlignedBoundingBox(chunkPos.x * Chunk.SIZE, chunkPos.y * Chunk.SIZE, chunkPos.z * Chunk.SIZE,
                    (chunkPos.x+1)*Chunk.SIZE, (chunkPos.y+1)*Chunk.SIZE,(chunkPos.z+1)*Chunk.SIZE));
        }
        return aabbs;
    }

    public boolean isFlagForReMesh()
    {
        return this.flagForReMesh;
    }

    public void flagForReMesh()
    {
        this.flagForReMesh = true;
        //todo: update the colliders
    }

    public void wasReMeshed()
    {
        this.flagForReMesh = false;
    }

    public boolean isEmpty()
    {
        if(chunks.isEmpty()) return true;
        for (Map.Entry<Integer, Chunk> entry : this.chunks.entrySet())
        {
            if(!entry.getValue().isEmpty()) return false;
        }
        return true;
    }
}
