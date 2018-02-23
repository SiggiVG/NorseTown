package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;
import com.deadvikingstudios.norsetown.view.meshes.components.TileMeshDef;
import org.lwjgl.util.vector.Vector3f;

public class TileLeaves extends Tile
{
    public TileLeaves(int index, String unlocalizedName, EnumMaterial material)
    {
        super(index, unlocalizedName, material);
        this.isSolid = false;
    }

    private static final TileMesh MESH = new TileMesh(
            new TileMeshDef.Cuboid(32, new Vector3f(0.125f, 0.125f, 0.125f), new Vector3f(0.875f, 0.875f, 0.875f)),
            new TileMeshDef.Cuboid(new Vector3f(0.5f,-0.25f,-0.25f), new Vector3f(0.5f,1.25f,1.25f),
                    new Vector3f(0,45,0), new TileMeshDef.Quad[]{
                    null, new TileMeshDef.Quad(33, EnumTileFace.NULL), null, new TileMeshDef.Quad(33, EnumTileFace.NULL), null, null}),
            new TileMeshDef.Cuboid(new Vector3f(0.5f,-0.25f,-0.25f), new Vector3f(0.5f,1.25f,1.25f),
                    new Vector3f(0,135,0), new TileMeshDef.Quad[]{
                    null, new TileMeshDef.Quad(33, EnumTileFace.NULL), null, new TileMeshDef.Quad(33, EnumTileFace.NULL), null, null})

    );

    @Override
    public TileMesh getTileMesh(int metadata)
    {
        return MESH;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {

    }

    @Override
    public EnumTileShape getTileShape(int metadata)
    {
        return EnumTileShape.CUBE_CROSS_EXTENDED;
    }

    @Override
    public boolean isReplacable()
    {
        return true;
    }

}
