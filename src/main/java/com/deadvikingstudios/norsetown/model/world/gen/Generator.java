package com.deadvikingstudios.norsetown.model.world.gen;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;

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
