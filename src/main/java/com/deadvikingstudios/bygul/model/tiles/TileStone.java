package com.deadvikingstudios.bygul.model.tiles;

import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.view.meshes.TileMesh;

public class TileStone extends Tile
{

    public TileStone(String unlocalizedName, EnumMaterial material)
    {
        super(unlocalizedName, material);
    }

    private static final TileMesh MESH = new TileMesh(9);
    @Override
    public TileMesh getTileMesh(int metadata, int x, int y, int z)
    {
        return MESH;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }
}
