package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;
import com.deadvikingstudios.norsetown.view.meshes.components.TileMeshDef;
import org.lwjgl.util.vector.Vector3f;

public class TileSod extends Tile
{
    public TileSod(int index, String unlocalizedName, EnumMaterial material)
    {
        super(index, unlocalizedName, material);
    }

    private static final TileMesh MESH = new TileMesh(new TileMeshDef.Cuboid(
            new TileMeshDef.Quad(2, EnumTileFace.NORTH),
            new TileMeshDef.Quad(2, EnumTileFace.EAST),
            new TileMeshDef.Quad(2, EnumTileFace.SOUTH),
            new TileMeshDef.Quad(2, EnumTileFace.WEST),
            new TileMeshDef.Quad(1, EnumTileFace.TOP),
            new TileMeshDef.Quad(4, EnumTileFace.BOTTOM)),
            new TileMeshDef.Cuboid(new Vector3f(0.5f,1f,-0.25f), new Vector3f(0.5f,2f,1.25f),
                    new Vector3f(0,45,0), new TileMeshDef.Quad[]{
                    null, new TileMeshDef.Quad(18, EnumTileFace.TOP), null, new TileMeshDef.Quad(18, EnumTileFace.TOP), null, null}),
            new TileMeshDef.Cuboid(new Vector3f(0.5f,1f,-0.25f), new Vector3f(0.5f,2f,1.25f),
                    new Vector3f(0,135,0), new TileMeshDef.Quad[]{
                    null, new TileMeshDef.Quad(18, EnumTileFace.TOP), null, new TileMeshDef.Quad(18, EnumTileFace.TOP), null, null})
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

    @Deprecated
    @Override
    public EnumTileShape getTileShape(int metadata)
    {
        return EnumTileShape.FULL_CUBE;
    }

}
