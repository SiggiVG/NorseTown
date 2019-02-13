package com.deadvikingstudios.bygul.view.meshes;

import com.deadvikingstudios.bygul.model.entities.Entity;

public class CubeMesh extends EntityMesh
{
    public CubeMesh(Entity entity, RawMesh rawMesh, MeshTexture texture)
    {
        super(entity, rawMesh, texture);
    }

    public static float[] getVertices(float yOffset, float height, boolean centerOnZero)
    {
        float[] ret = vertices.clone();

        if(height != 0)
        {
            for (int i = 1; i < vertices.length; i += 3)
            {
                if(vertices[i] != 0)
                {
                    ret[i] = yOffset + height;
                }
                else
                {
                    ret[i] = yOffset;
                }
            }
        }

        if(!centerOnZero) return vertices;
        for (int i = 0; i < vertices.length; i++)
        {
            ret[i] -= 0.5f;
        }
        return ret;
    }

    public static final float[] vertices =
            {
                    0,1,1,
                    0,0,1,
                    1,0,1,
                    1,1,1,

                    1,1,1,
                    1,0,1,
                    1,0,0,
                    1,1,0,

                    1,1,0,
                    1,0,0,
                    0,0,0,
                    0,1,0,

                    0,1,0,
                    0,0,0,
                    0,0,1,
                    0,1,1,

                    1,1,1,
                    1,1,0,
                    0,1,0,
                    0,1,1,

                    0,0,1,
                    0,0,0,
                    1,0,0,
                    1,0,1
            };

    public static final  float[] cubeNormals =
            {
                    0,0,1,
                    0,0,1,
                    0,0,1,
                    0,0,1,

                    1,0,0,
                    1,0,0,
                    1,0,0,
                    1,0,0,

                    0,0,-1,
                    0,0,-1,
                    0,0,-1,
                    0,0,-1,

                    -1,0,0,
                    -1,0,0,
                    -1,0,0,
                    -1,0,0,

                    0,1,0,
                    0,1,0,
                    0,1,0,
                    0,1,0,

                    0,-1,0,
                    0,-1,0,
                    0,-1,0,
                    0,-1,0,
            };

    public static final  int[] indices = {
            0, 1, 3,
            3, 1, 2,

            4, 5, 7,
            7, 5, 6,

            8, 9, 11,
            11, 9, 10,

            12, 13, 15,
            15, 13, 14,

            16, 17, 19,
            19, 17, 18,

            20, 21, 23,
            23, 21, 22
    };

    public static final  float[] uv = new float[]
    {
            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0
    };
}
