package com.deadvikingstudios.bygul.view.renderers;

import com.deadvikingstudios.bygul.controller.CameraController;
import com.deadvikingstudios.bygul.controller.GameContainer;
import com.deadvikingstudios.bygul.utils.Maths;
import com.deadvikingstudios.bygul.view.WindowManager;
import com.deadvikingstudios.bygul.view.meshes.ChunkColMesh;
import com.deadvikingstudios.bygul.view.meshes.SkyboxMesh;
import com.deadvikingstudios.bygul.view.shaders.StaticShader;
import com.deadvikingstudios.bygul.view.meshes.EntityMesh;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class Renderer
{
    protected Matrix4f projectionMatrix;
    protected StaticShader shader;

    public static final float P_FOV = 70;
    public static final float P_NEAR_PLANE = 0.5f;
    public static final float P_FAR_PLANE = 300f;

    public Renderer(StaticShader shader)
    {
        this.shader = shader;
        createProjectionMatrix(shader, false);
        GL11.glEnable(GL11.GL_CULL_FACE); //Culls inner faces
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_BLEND);//Allows for transparent textures
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Sets transparency to use Alpha bit
        //GL11.glDepthFunc(GL11.GL_LESS);

    }

    public void renderScene(List<EntityMesh> entities, List<ChunkColMesh> structures, CameraController camera, Vector4f clip)
    {
        shader.start();
        shader.loadClipPlane(clip);
        shader.loadViewMatrix(camera);

        shader.loadAmbientLight(GameContainer.ambientLight);
        shader.loadDirectionalLight(GameContainer.sunLight);

        for (ChunkColMesh mesh : structures)
        {
            render(mesh, shader);
        }

        for (EntityMesh entity : entities)
        {
            render(entity, shader);
        }

        shader.stop();
    }

    public void clear()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glClearColor(0.0f,0.0f,0.0f,1.0f);
        GL11.glClearColor(0.4f, 0.7f, 1.0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

//        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
//
//        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo);
//        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_RGBA8, WindowManager.WIDTH, WindowManager.HEIGHT);
//        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, rbo);
//
//        assert(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE);
//
//        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo);
//        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
        //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);*/

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        //Moved poll events caller from here to GameContainer's input method
    }

    public void createProjectionMatrix(StaticShader shader, boolean isOrthogonal)
    {
        //just do perspective for now
        if(!isOrthogonal)
        {
            projectionMatrix = Maths.createProjectionMatrix(
                WindowManager.getCurrentWidth(), WindowManager.getCurrentHeight(), P_FOV, P_NEAR_PLANE, P_FAR_PLANE);
        }
        else
        {
            //projectionMatrix = Maths.createOrthogonalMatrix(
            //        (float) Display.getWidth() / (float) Display.getHeight(),);
        }
        shader.start();
//        System.out.println(projectionMatrix);
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

        Matrix4f transform = Maths.createTransformationMatrix(new Vector3f(entity.getPosition()).translate(0.5f, 0.5f, 0.5f),
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

    public static void render(SkyboxMesh entity, StaticShader shader)
    {
        GL30.glBindVertexArray(entity.getMesh().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Matrix4f transform = Maths.createTransformationMatrix(entity.getPosition(),
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

//    public static void render(ChunkMesh chunkMesh, StaticShader shader)
//    {
//        GL30.glBindVertexArray(chunkMesh.getMesh().getVaoID());
//        GL20.glEnableVertexAttribArray(0);
//        GL20.glEnableVertexAttribArray(1);
//        GL20.glEnableVertexAttribArray(2);
//
//        Matrix4f transform = Maths.createTransformationMatrix(chunkMesh.getPosition(),
//                chunkMesh.getRotationX(),chunkMesh.getRotationY(),chunkMesh.getRotationZ(), chunkMesh.getScale());
//        shader.loadTransformationMatrix(transform);
//
//        shader.loadShineVariables(chunkMesh.getShineDamper(), chunkMesh.getReflectivity());
//
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, chunkMesh.getTexture().getTextureID());
//        GL11.glDrawElements(GL11.GL_TRIANGLES, chunkMesh.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
//        GL20.glDisableVertexAttribArray(0);
//        GL20.glDisableVertexAttribArray(1);
//        GL20.glDisableVertexAttribArray(2);
//        GL30.glBindVertexArray(0);
//    }

    public static void render(ChunkColMesh structureChunkMesh, StaticShader shader)
    {
        GL30.glBindVertexArray(structureChunkMesh.getMesh().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Matrix4f transform = Maths.createTransformationMatrix(structureChunkMesh.getPosition(),
                structureChunkMesh.getRotationX(),structureChunkMesh.getRotationY(),structureChunkMesh.getRotationZ(), structureChunkMesh.getScale());
        shader.loadTransformationMatrix(transform);

        shader.loadShineVariables(structureChunkMesh.getShineDamper(), structureChunkMesh.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, structureChunkMesh.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, structureChunkMesh.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void cleanUp()
    {
        shader.cleanUp();
    }

    public StaticShader getShader()
    {
        return shader;
    }

    /*public static void render(TexturedMesh mesh, StaticShader shader )
    {
        GL30.glBindVertexArray(mesh.getMesh().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        /*Matrix4f transform = Maths.createTransformationMatrix(chunkMesh.getPosition(), 0,0,0, 1);
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
