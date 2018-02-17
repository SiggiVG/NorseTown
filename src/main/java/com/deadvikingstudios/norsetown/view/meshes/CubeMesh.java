package com.deadvikingstudios.norsetown.view.meshes;

import com.deadvikingstudios.norsetown.model.entities.Entity;

public class CubeMesh extends EntityMesh
{
    public CubeMesh(Entity entity, RawMesh rawMesh, MeshTexture texture)
    {
        super(entity, rawMesh, texture);
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

    public static final  float[] uv = new float[8*6];
    static
    {
        for (int i = 0; i < uv.length / 4; i++)
        {
            uv[i] = 0;
            uv[i + 1] = 0;
            uv[i + 2] = 0;
            uv[i + 3] = 1;
            uv[i + 4] = 1;
            uv[i + 5] = 1;
            uv[i + 6] = 1;
            uv[i + 7] = 0;
        }
    }
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//
//            0,0,
//            0,1,
//            1,1,
//            1,0
//    }
}
