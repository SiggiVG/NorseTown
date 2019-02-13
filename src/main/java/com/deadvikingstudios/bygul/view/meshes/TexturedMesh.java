package com.deadvikingstudios.bygul.view.meshes;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * A Wrapper for a Model and a Texture
 */
public class TexturedMesh
{
    protected RawMesh rawMesh;
    protected MeshTexture texture;

    private float shineDamper = 1f;
    private float reflectivity = 0f;

    public TexturedMesh(RawMesh model, MeshTexture texture)
    {
        this.rawMesh = model;
        this.texture = texture;
    }

    public TexturedMesh(RawMesh model, MeshTexture texture, float shineDamper, float reflectivity)
    {
        this.rawMesh = model;
        this.texture = texture;
        this.shineDamper = shineDamper;
        this.reflectivity = reflectivity;
    }

    protected TexturedMesh(MeshTexture texture)
    {
        this.texture = texture;
    }

    protected TexturedMesh(){}

    public RawMesh getMesh()
    {
        return rawMesh;
    }

    public MeshTexture getTexture() { return texture; }

    public float getShineDamper()
    {
        return shineDamper;
    }

    public float getReflectivity()
    {
        return reflectivity;
    }
}
