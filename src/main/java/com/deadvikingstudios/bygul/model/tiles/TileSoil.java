package com.deadvikingstudios.bygul.model.tiles;

import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.view.meshes.TileMesh;
import com.deadvikingstudios.norsetown.model.tiles.NorseTiles;

public class TileSoil extends Tile
{
    public TileSoil(String unlocalizedName, EnumMaterial material)
    {
        super(unlocalizedName, material);
    }

    private static final TileMesh MESH_SOIL = new TileMesh(3);
    private static final TileMesh MESH_CLAY = new TileMesh(10);

    @Override
    public TileMesh getTileMesh(int metadata, int x, int y, int z)
    {
        if (this == NorseTiles.tileClay) return MESH_CLAY;
        return MESH_SOIL;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }
}
