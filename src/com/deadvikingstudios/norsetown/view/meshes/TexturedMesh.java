package com.deadvikingstudios.norsetown.view.meshes;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * A Wrapper for a Model and a Texture
 */
public class TexturedMesh
{
    protected RawMesh rawMesh;
    protected MeshTexture texture;

    public TexturedMesh(RawMesh model, MeshTexture texture)
    {
        this.rawMesh = model;
        this.texture = texture;
    }

    protected TexturedMesh(){}

    public RawMesh getMesh()
    {
        return rawMesh;
    }

    public MeshTexture getTexture() { return texture; }
}
