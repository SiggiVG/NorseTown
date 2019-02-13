package com.deadvikingstudios.bygul.view.meshes;

import com.deadvikingstudios.bygul.model.tiles.Tile;
import com.deadvikingstudios.bygul.utils.ArrayUtils;
import com.deadvikingstudios.bygul.view.meshes.components.TileMeshDef;
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
    //TODO: change to store Faces, such that it keeps track of the actual information, not the blueprint
    // And have it grab each chunk of data based on which faces are culled
    private List<TileMeshDef.Cuboid> cuboids = new ArrayList<TileMeshDef.Cuboid>();

    private List<List<Face>> facemap;


    public TileMesh()
    {
        this(0);
    }

    public TileMesh(int texture)
    {
        this.cuboids.add(new TileMeshDef.Cuboid(texture));
        this.genFaces();
    }

    public TileMesh(TileMeshDef.Cuboid ... cuboids)
    {
        Collections.addAll(this.cuboids, cuboids);
        this.genFaces();
    }

    public TileMesh(List<TileMeshDef.Cuboid> cuboids)
    {
        this.cuboids.addAll(cuboids);
        this.genFaces();
    }

    private void genFaces()
    {
        facemap = new ArrayList<List<Face>>();
        //adds all permutations of the tile to the Map
        int l = 6;
        for (int i = 0; i < Math.pow(2, l); i++)
        {
            StringBuilder bin = new StringBuilder(Integer.toBinaryString(i));
            //pads the left side with 0s
            while(bin.length() < l)
                bin.insert(0, "0");
            //converts to a character array for quick access
            char[] chars = bin.toString().toCharArray();
            boolean[] bools = new boolean[l];
            //populates boolean array
            for (int j = 0; j < chars.length; j++)
            {
                bools[j] = chars[j] == '0';
            }
            //populates map
            this.facemap.add(i,getFaces(bools));
        }
    }

    private List<Face> getFaces(boolean[] cullFaces)
    {
        List<Face> faces = new ArrayList<>();
        for (TileMeshDef.Cuboid cuboid : this.cuboids)
        {
            faces.addAll(TileMeshDef.drawCuboid(cuboid, cullFaces));
        }
        return faces;
    }

    /**
     * Draws the TileMesh at the location specified
     * @param tile
     * @param cullFaces
     * @param metadata
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static List<Face> drawTile(@NotNull final Tile tile, @NotNull final boolean[] cullFaces, int metadata, float x, float y, float z)
    {
        TileMesh mesh = tile.getTileMesh(metadata, (int)x, (int)y, (int)z);
        if(mesh != null)
        {
            int n = 0, l = cullFaces.length;
            for (int i = 0; i < l; ++i) {
                n = (n << 1) + (cullFaces[i] ? 1 : 0);
            }
            return mesh.facemap.get(n);
        }
        return null;
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
