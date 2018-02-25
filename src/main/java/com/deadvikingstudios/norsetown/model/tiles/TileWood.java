package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;

public class TileWood extends Tile
{
    public TileWood(int index, String unlocalizedName, EnumMaterial material)
    {
        super(index, unlocalizedName, material);
    }

    private static final TileMesh MESH = new TileMesh(5);
    @Override
    public TileMesh getTileMesh(int metadata)
    {
        return MESH;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }
}
