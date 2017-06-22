package com.deadvikingstudios.norsetown.view.lwjgl.renderers;

import com.deadvikingstudios.norsetown.view.RenderMath;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.ChunkMesh;
import com.deadvikingstudios.norsetown.view.meshes.EntityMesh;
import com.deadvikingstudios.norsetown.view.meshes.TexturedMesh;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class MasterRenderer
{
    private Matrix4f projectionMatrix;

    private static final float P_FOV = 70;
    private static final float P_NEAR_PLANE = 0.1f;
    private static final float P_FAR_PLANE = 100f;

    public MasterRenderer(StaticShader shader)
    {
        createProjectionMatrix(shader);
    }

    public void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0.4f, 0.7f, 1.0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(TexturedMesh model, StaticShader shader)
    {
        if(model instanceof EntityMesh)
        {
            EntityRenderer.render((EntityMesh)model, shader);
        }
        else
        {
            MeshRenderer.render(model);
        }
    }

    public void render(ChunkMesh chunkMesh, StaticShader shader)
    {

        ChunkRenderer.render(chunkMesh, shader);

    }

    public void createProjectionMatrix(StaticShader shader)
    {
        //just do perspective for now
        projectionMatrix = RenderMath.createPerspectiveMatrix((float) Display.getWidth() / (float)Display.getHeight(), P_FOV, P_NEAR_PLANE, P_FAR_PLANE);

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        //TODO Orthogonal
    }

    public void splash(TexturedMesh image)
    {
        MeshRenderer.render(image);
    }
}
