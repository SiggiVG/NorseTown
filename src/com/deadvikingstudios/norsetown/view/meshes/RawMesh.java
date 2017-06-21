package com.deadvikingstudios.norsetown.view.meshes;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * Stores the pointer to a VBO within a VAO and the size of what is stored there (Vertex/Indices(Triangles)/UVs)
 */
public class RawMesh
{
    private int vaoID;
    private int vertexCount;

    public RawMesh(int vaoID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID()
    {
        return vaoID;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }
}
