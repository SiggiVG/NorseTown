package com.deadvikingstudios.bygul.model.world.gen;

import com.deadvikingstudios.bygul.model.tiles.Tile;
import com.deadvikingstudios.bygul.model.world.structures.Structure;

/**
 * Generators generate terrain
 */
public abstract class Generator
{
    protected Structure structure;
    protected final long seed;

    protected Generator(Structure structure, long seed)
    {
        this.structure = structure;
        this.seed = seed;
        this.gen();
    }

    protected abstract void gen();

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
