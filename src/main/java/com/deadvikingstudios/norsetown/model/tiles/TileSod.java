package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.EnumTileShape;
import com.deadvikingstudios.norsetown.model.world.World;

public class TileSod extends Tile
{
    public TileSod(int index, String unlocalizedName, EnumMaterial material)
    {
        super(index, unlocalizedName, material);
    }

    @Override
    public void update(World world, int x, int y, int z)
    {

    }

    @Override
    public EnumTileShape getRenderType(int metadata)
    {
        return EnumTileShape.FULL_CUBE;
    }
}
