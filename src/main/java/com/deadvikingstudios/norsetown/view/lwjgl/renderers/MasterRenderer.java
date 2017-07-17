package com.deadvikingstudios.norsetown.view.lwjgl.renderers;

import com.deadvikingstudios.norsetown.model.tileenitites.TileEntity;
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
    private static final float P_FAR_PLANE = 250f;

    public MasterRenderer(StaticShader shader)
    {
        createProjectionMatrix(shader, false);
        GL11.glEnable(GL11.GL_CULL_FACE); //Culls inner faces
        GL11.glEnable(GL11.GL_BLEND);//Allows for transparent textures
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Sets transparency to use Alpha bit
    }

    public void clear()
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
        //TODO: render all tile entities
        /*for (TileEntity te : chunkMesh.getChunk().getTileEntities())
        {
            render(te, shader);
        }*/
    }

    public void createProjectionMatrix(StaticShader shader, boolean isOrthogonal)
    {
        //just do perspective for now
        if(!isOrthogonal)
        {
            projectionMatrix = RenderMath.createPerspectiveMatrix(
                Display.getWidth(), Display.getHeight(), P_FOV, P_NEAR_PLANE, P_FAR_PLANE);
        }
        else
        {
            //projectionMatrix = RenderMath.createOrthogonalMatrix(
            //        (float) Display.getWidth() / (float) Display.getHeight(),);
        }
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        //TODO Orthogonal
    }

    public Matrix4f getProjectionMatrix()
    {
        return projectionMatrix;
    }

    public void splash(TexturedMesh image)
    {
        MeshRenderer.render(image);
    }
}
