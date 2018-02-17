package com.deadvikingstudios.norsetown.view.meshes;

import com.deadvikingstudios.norsetown.model.entities.Entity;

public class SkyboxMesh extends EntityMesh
{

    public SkyboxMesh(Entity entity, RawMesh rawMesh, MeshTexture texture)
    {
        super(entity, rawMesh, texture);
    }

    //are inverted, as it faces in towards the player
    public static final float[] skyBoxNormals =
    {
            //+z
            0,0,-1,
            0,0,-1,
            0,0,-1,
            0,0,-1,

            //+x
            -1,0,0,
            -1,0,0,
            -1,0,0,
            -1,0,0,

            //-z
            0,0,1,
            0,0,1,
            0,0,1,
            0,0,1,

            //-x
            1,0,0,
            1,0,0,
            1,0,0,
            1,0,0,

            //+y
            0,-1,0,
            0,-1,0,
            0,-1,0,
            0,-1,0,

            //-y
            0,1,0,
            0,1,0,
            0,1,0,
            0,1,0,
    };

    private static final float skyboxSize = 1;//(float)((Renderer.P_FAR_PLANE-10f)/(Math.sqrt(3)));
    public static final float[] skyVertices = {
            skyboxSize,skyboxSize,skyboxSize,
            skyboxSize,-skyboxSize,skyboxSize,
            -skyboxSize,-skyboxSize,skyboxSize,
            -skyboxSize,skyboxSize,skyboxSize,

            skyboxSize,skyboxSize,-skyboxSize,
            skyboxSize,-skyboxSize,-skyboxSize,
            skyboxSize,-skyboxSize,skyboxSize,
            skyboxSize,skyboxSize,skyboxSize,

            -skyboxSize,skyboxSize,-skyboxSize,
            -skyboxSize,-skyboxSize,-skyboxSize,
            skyboxSize,-skyboxSize,-skyboxSize,
            skyboxSize,skyboxSize,-skyboxSize,

            -skyboxSize,skyboxSize,skyboxSize,
            -skyboxSize,-skyboxSize,skyboxSize,
            -skyboxSize,-skyboxSize,-skyboxSize,
            -skyboxSize,skyboxSize,-skyboxSize,

            -skyboxSize,skyboxSize,skyboxSize,
            -skyboxSize,skyboxSize,-skyboxSize,
            skyboxSize,skyboxSize,-skyboxSize,
            skyboxSize,skyboxSize,skyboxSize,

            skyboxSize,-skyboxSize,skyboxSize,
            skyboxSize,-skyboxSize,-skyboxSize,
            -skyboxSize,-skyboxSize,-skyboxSize,
            -skyboxSize,-skyboxSize,skyboxSize
    };

    public static final float[] skyboxUV = {
            0.5f,0,
            0.5f,0.5f,
            1f,0.5f,
            1f,0,

            1f,0,
            1f,0.5f,
            0.5f,0.5f,
            0.5f,0,

            0.5f,0,
            0.5f,0.5f,
            1f,0.5f,
            1f,0,

            1f,0,
            1f,0.5f,
            0.5f,0.5f,
            0.5f,0,

            0,0,
            0,0.5f,
            0.5f,0.5f,
            0.5f,0,

            0.5f,0.5f,
            0.5f,1f,
            1f,1f,
            1f,0.5f,
    };
}
