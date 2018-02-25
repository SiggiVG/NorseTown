package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;

public class TileCrop extends Tile
{
    public TileCrop(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.PLANT);
    }

    @Override
    public TileMesh getTileMesh(int metadata)
    {
        return null;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }
}
