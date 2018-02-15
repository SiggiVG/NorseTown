package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;

public class TileAir extends Tile
{
    public TileAir(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.AIR);
        this.isOpaque = false;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }

    @Override
    public EnumTileShape getTileShape(int metadata)
    {
        return EnumTileShape.NULL;
    }

    @Override
    public boolean isReplacable()
    {
        return true;
    }
}
