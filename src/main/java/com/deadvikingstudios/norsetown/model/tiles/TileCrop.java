package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;

public class TileCrop extends Tile
{
    public TileCrop(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.PLANT);
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }

    @Override
    public EnumTileShape getTileShape(int metadata)
    {
        return null;
    }
}
