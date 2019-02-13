package com.deadvikingstudios.bygul.view.meshes;

import com.deadvikingstudios.bygul.controller.GameContainer;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * Stores the pointer to a VBO within a VAO and the size of what is stored there (Vertex/Indices(Triangles)/UVs)
 */
public class RawMesh
{
    private int vaoID;
    private int vboVertID;
    private int vboIndID;
    private int vboUvID;
    private int vboNormID;
    private int vertexCount;

    public RawMesh(int vaoID, int vboVertID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vboVertID = vboVertID;
        this.vertexCount = vertexCount;
    }

    public RawMesh(int vaoID, int vboVertID, int vboIndID, int vboUvID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vboVertID = vboVertID;
        this.vboIndID = vboIndID;
        this.vboUvID = vboUvID;
        this.vertexCount = vertexCount;
    }

    public RawMesh(int vaoID, int vboVertID, int vboIndID, int vboUvID, int vboNormID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vboVertID = vboVertID;
        this.vboIndID = vboIndID;
        this.vboUvID = vboUvID;
        this.vboNormID = vboNormID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID()
    {
        return vaoID;
    }

    public int getVboVertID()
    {
        return vboVertID;
    }

    public int getVboIndID()
    {
        return vboIndID;
    }

    public int getVboUvID()
    {
        return vboUvID;
    }

    public int getVboNormID()
    {
        return vboNormID;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public void dispose()
    {
        GameContainer.loader.unloadMesh(this);
    }
}
