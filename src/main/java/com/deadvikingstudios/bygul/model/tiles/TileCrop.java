package com.deadvikingstudios.bygul.model.tiles;

import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.view.meshes.TileMesh;

public class TileCrop extends Tile
{
    public TileCrop(String unlocalizedName)
    {
        super(unlocalizedName, EnumMaterial.PLANT);
    }

    @Override
    public TileMesh getTileMesh(int metadata, int x, int y, int z)
    {
        return null;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }
}
