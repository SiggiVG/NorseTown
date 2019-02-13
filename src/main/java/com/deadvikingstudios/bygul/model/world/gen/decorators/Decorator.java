package com.deadvikingstudios.bygul.model.world.gen.decorators;

import com.deadvikingstudios.bygul.model.tiles.Tile;
import com.deadvikingstudios.bygul.model.world.structures.Chunk;
import com.deadvikingstudios.bygul.model.world.structures.Structure;

import java.util.Random;

/**
 * Decorators are similar to generators, but are run on a per ChunkColumn basis
 */
public abstract class Decorator
{
    protected Structure structure;
    protected final long seed;

    protected Decorator(Structure structure, long seed)
    {
        this.structure = structure;
        this.seed = seed;
    }

    public void decorate(int x, int z)
    {
        Random random = new Random(seed + x + (z*16));
        for (int i = 0; i < Chunk.SIZE; i++)
        {
            for (int k = 0; k < Chunk.SIZE; k++)
            {
                decorate(random, x+i, z+k);
            }
        }
    }

    protected void decorate(Random random, int x, int y)
    {
        
    }

    public Structure getStructure()
    {
        return structure;
    }

    protected Tile getTile(int x, int y, int z)
    {
        return this.structure.getTile(x,y,z);
    }

    public void setTile(Tile tile, int x, int y, int z)
    {
        this.structure.setTile(tile,x,y,z);
    }
}
