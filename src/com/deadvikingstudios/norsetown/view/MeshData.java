package com.deadvikingstudios.norsetown.view;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class MeshData
{
    public List<Vector3f> vertices = new ArrayList<Vector3f>();
    public List<Integer> indices = new ArrayList<Integer>();
    public List<Float> uvs = new ArrayList<Float>();

    public MeshData(){}

    public MeshData(List<Vector3f> v, List<Integer> i, List<Float> uv)
    {
        this.vertices = v;
        this.indices = i;
        this.uvs = uv;
    }

    public void merge(MeshData m)
    {
        if(m.vertices.size() <= 0)
        {
            return;
        }

        if (vertices.size() <= 0)
        {
            vertices = m.vertices;
            indices = m.indices;
            return;
        }

        int count = vertices.size();
        //vertices.addAll(m.vertices);
        for (int i = 0; i < m.vertices.size(); ++i)
        {
            vertices.add(m.vertices.get(i));
        }
        for (int i = 0; i < m.indices.size(); ++i)
        {
            indices.add(m.indices.get(i) + count);
        }
        for (int i = 0; i < m.uvs.size(); ++i)
        {
            uvs.add(m.uvs.get(i));
        }
        //uvs.addAll(m.uvs);
    }

    public float[] getVerticesAsFloatArray()
    {
        float[] ar = new float[vertices.size() * 3];
        for (int i = 0; i < ar.length; i+=3)
        {
            ar[i] = vertices.get(i).getX();
            ar[i+1] = vertices.get(i).getY();
            ar[i+2] = vertices.get(i).getZ();
        }
        return ar;
    }

    public int[] getIndicesAsIntArray()
    {
        int[] ar = new int[indices.size()];
        for (int i = 0; i < ar.length; ++i)
        {
            ar[i] = indices.get(i);
        }
        return ar;
    }

    public float[] getUVsAsFloatArray()
    {
        float[] ar = new float[uvs.size()];
        for (int i = 0; i < ar.length; ++i)
        {
            ar[i] = uvs.get(i);
        }
        return ar;
    }

}
