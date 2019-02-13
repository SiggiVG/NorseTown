package com.deadvikingstudios.bygul.view.meshes;

import com.deadvikingstudios.bygul.model.entities.Entity;
import com.deadvikingstudios.bygul.utils.ArrayUtils;
import com.deadvikingstudios.bygul.view.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;

import static com.deadvikingstudios.bygul.controller.GameContainer.loader;

public class OceanMesh extends EntityMesh
{
    protected OceanMesh(Entity entity, MeshTexture texture)
    {
        super(entity, texture);

        List<Float> vertices = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();
        List<Float> uvs = new ArrayList<Float>();
        List<Float> norms = new ArrayList<Float>();

        rawMesh = loader.loadToVAO(ArrayUtils.floatFromFloat(vertices),
                ArrayUtils.intFromInteger(indices), ArrayUtils.floatFromFloat(uvs), ArrayUtils.floatFromFloat(norms));
    }
    /**
     * TODO: create a connected mesh of quads
     */

    protected void createMesh(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms)
    {
        for (int i = -(int) Renderer.P_FAR_PLANE; i < (int) Renderer.P_FAR_PLANE; i++)
        {
            for (int k = -(int) Renderer.P_FAR_PLANE; k < (int) Renderer.P_FAR_PLANE; k++)
            {

            }
        }
    }
}
