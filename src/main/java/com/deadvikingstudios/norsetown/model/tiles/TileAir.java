package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.EnumTileShape;
import com.deadvikingstudios.norsetown.model.world.World;

public class TileAir extends Tile
{
    public TileAir(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.AIR);
        this.isOpaque = false;
    }

    @Override
    public void update(World world, int x, int y, int z)
    {

    }

    @Override
    public EnumTileShape getRenderType(int metadata)
    {
        return EnumTileShape.NULL;
    }

    @Override
    public boolean isReplacable()
    {
        return true;
    }
}
