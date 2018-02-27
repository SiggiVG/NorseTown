package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;

public class TileAir extends Tile
{
    public TileAir(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.AIR);
        this.isOpaque = false;
        this.isSolid = false;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }

    @Override
    public boolean isReplacable()
    {
        return true;
    }

    @Override
    public TileMesh getTileMesh(int metadata, int x, int y, int z)
    {
        return null;
    }

    @Override
    public boolean isSideSolid(EnumTileFace side)
    {
        return false;
    }

    @Override
    public AxisAlignedBoundingBox getAABB(int x, int y, int z)
    {
        return null;
    }
}
