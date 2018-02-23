package com.deadvikingstudios.norsetown.view.meshes;

import com.deadvikingstudios.norsetown.model.tiles.EnumTileFace;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.ArrayUtils;
import com.deadvikingstudios.norsetown.view.meshes.components.TileMeshDef;
import com.sun.istack.internal.NotNull;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Preparation for loading tile model definitions from file
 */
public class TileMesh
{
    private List<TileMeshDef.Cuboid> cuboids = new ArrayList<TileMeshDef.Cuboid>();

    public TileMesh()
    {
        this(0);
    }

    public TileMesh(int texture)
    {
        this.cuboids.add(new TileMeshDef.Cuboid(texture));
    }

    public TileMesh(TileMeshDef.Cuboid ... cuboids)
    {
        Collections.addAll(this.cuboids, cuboids);
    }

    public TileMesh(List<TileMeshDef.Cuboid> cuboids)
    {
        this.cuboids.addAll(cuboids);
    }

    public static List<Face> drawTile(@NotNull final Tile tile, @NotNull final boolean[] cullFaces, int metadata)
    {
        List<Face> faces = new ArrayList<>();
        TileMesh mesh = tile.getTileMesh(metadata);
        if(mesh != null)
        {
            for (TileMeshDef.Cuboid cuboid : mesh.cuboids)
            {
                faces.addAll(TileMeshDef.drawCuboid(cuboid, cullFaces));
            }
        }
        return faces;
    }

    public static class Face
    {
        float[] vertices = new float[4*3];
        List<Float> norms = new ArrayList<Float>();
        int[] indices = new int[6];
        List<Float> uvs = new ArrayList<Float>();

        public Face(float[] vertices, int[] indices, float[] uvs, float[] norms)
        {
            this.vertices = vertices;
            this.indices = indices;
            this.uvs.addAll(ArrayUtils.floatToFloat(uvs));
            this.norms.addAll(ArrayUtils.floatToFloat(norms));
        }

        public void draw(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms, Vector3f position)
        {
            int count = vertices.size()/3;
            float[] verts  = new float[this.vertices.length];
            for (int i = 0; i < 4; i++)
            {
                verts[i*3] = this.vertices[i*3]+position.x;
                verts[i*3+1] = this.vertices[i*3+1]+position.y;
                verts[i*3+2] = this.vertices[i*3+2]+position.z;
            }
            vertices.addAll(ArrayUtils.floatToFloat(verts));
            for (int i = 0; i < this.indices.length; i++)
            {
                indices.add(this.indices[i] + count);
            }
            uvs.addAll(this.uvs);
            norms.addAll(this.norms);
        }
    }







}
