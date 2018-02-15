package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.Vector2i;
import com.deadvikingstudios.norsetown.utils.Vector3i;
import com.deadvikingstudios.norsetown.view.meshes.StructureMesh;
import org.newdawn.slick.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.deadvikingstudios.norsetown.model.world.structures.Chunk.SIZE;

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

    public ChunkColumn(Vector2i pos)
    {
        chunks = new HashMap<Integer, Chunk>();
        position = new Vector2i(pos);
        GameContainer.structuresMeshes.add(new StructureMesh(this, GameContainer.terrainTexture));
    }

    public ChunkColumn(ChunkColumn col)
    {
        chunks = new HashMap<Integer, Chunk>();
        this.position = new Vector2i(col.position);
        for (Map.Entry<Integer, Chunk> entry : this.chunks.entrySet())
        {
            chunks.put(entry.getKey(), new Chunk(entry.getValue()));
        }
        GameContainer.structuresMeshes.add(new StructureMesh(this, GameContainer.terrainTexture));
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
        Chunk chunk = getChunk(x,y,z,true);

        if(chunk.getTile(x,y,z) == tile) return;

        chunk.setTile(tile, Math.floorMod(x, SIZE), Math.floorMod(y, SIZE), Math.floorMod(z, SIZE));
        this.flagForReMesh = true;
    }

    @Override
    public void update()
    {

    }

    public boolean isFlagForReMesh()
    {
        return this.flagForReMesh;
    }

    public void wasReMeshed()
    {
        this.flagForReMesh = false;
    }
}
