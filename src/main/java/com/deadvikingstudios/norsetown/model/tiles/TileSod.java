package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;

public class TileSod extends Tile
{
    public TileSod(int index, String unlocalizedName, EnumMaterial material)
    {
        super(index, unlocalizedName, material);
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }

    @Override
    public EnumTileShape getTileShape(int metadata)
    {
        return EnumTileShape.FULL_CUBE;
    }
}
