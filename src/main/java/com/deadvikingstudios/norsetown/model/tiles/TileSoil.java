package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;

public class TileSoil extends Tile
{
    public TileSoil(int index, String unlocalizedName, EnumMaterial material)
    {
        super(index, unlocalizedName, material);
    }

    private static final TileMesh MESH_SOIL = new TileMesh(3);
    private static final TileMesh MESH_CLAY = new TileMesh(10);
    @Override
    public TileMesh getTileMesh(int metadata)
    {
        if(this == Tiles.tileClay) return MESH_CLAY;
        return MESH_SOIL;
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
