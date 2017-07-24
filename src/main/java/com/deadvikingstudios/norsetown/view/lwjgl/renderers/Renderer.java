package com.deadvikingstudios.norsetown.view.lwjgl.renderers;

import com.deadvikingstudios.norsetown.utils.RenderMath;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.ChunkMesh;
import com.deadvikingstudios.norsetown.view.meshes.EntityMesh;
import com.deadvikingstudios.norsetown.view.meshes.TexturedMesh;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import javax.swing.*;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class Renderer
{
    private Matrix4f projectionMatrix;
    private StaticShader shader;

    private static final float P_FOV = 70;
    private static final float P_NEAR_PLANE = 0.5f;
    public static final float P_FAR_PLANE = 300f;

    public Renderer(StaticShader shader)
    {
        this.shader = shader;
        createProjectionMatrix(shader, false);
        GL11.glEnable(GL11.GL_CULL_FACE); //Culls inner faces
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_BLEND);//Allows for transparent textures
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Sets transparency to use Alpha bit
    }

    public void clear()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0,0,0,1);//0.4f, 0.7f, 1.0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
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

    public static void render(EntityMesh entity, StaticShader shader)
    {
        GL30.glBindVertexArray(entity.getMesh().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Matrix4f transform = RenderMath.createTransformationMatrix(entity.getPosition(),
                entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
        shader.loadTransformationMatrix(transform);

        shader.loadShineVariables(entity.getShineDamper(), entity.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public static void render(ChunkMesh chunkMesh, StaticShader shader)
    {
        GL30.glBindVertexArray(chunkMesh.getMesh().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Matrix4f transform = RenderMath.createTransformationMatrix(chunkMesh.getPosition(),
                chunkMesh.getRotationX(),chunkMesh.getRotationY(),chunkMesh.getRotationZ(), chunkMesh.getScale());
        shader.loadTransformationMatrix(transform);

        shader.loadShineVariables(chunkMesh.getShineDamper(), chunkMesh.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, chunkMesh.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, chunkMesh.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /*public static void render(TexturedMesh mesh, StaticShader shader )
    {
        GL30.glBindVertexArray(mesh.getMesh().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        /*Matrix4f transform = RenderMath.createTransformationMatrix(chunkMesh.getPosition(), 0,0,0, 1);
        shader.loadTransformationMatrix(transform);*//*
        shader.loadShineVariables(mesh.getShineDamper(), mesh.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }*/
}
