package com.deadvikingstudios.bygul.view.renderers;

import com.deadvikingstudios.bygul.controller.CameraController;
import com.deadvikingstudios.bygul.model.world.WaterTile;
import com.deadvikingstudios.bygul.utils.Maths;
import com.deadvikingstudios.bygul.view.Loader;
import com.deadvikingstudios.bygul.view.WaterFrameBuffers;
import com.deadvikingstudios.bygul.view.shaders.WaterShader;
import com.deadvikingstudios.bygul.view.meshes.RawMesh;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class RendererWater {

    private static final float WAVE_SPEED = 0.03f;
    private static final String  DUDV_MAP = "norsetown/textures/waterDUDV";

    private RawMesh quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;

    private float moveFactor = 0;
    private int dudvTexture;

    public RendererWater(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        dudvTexture = loader.loadTexture(DUDV_MAP);
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }

    public void render(List<WaterTile> water, CameraController camera) {
        prepareRender(camera);
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = Maths.createTransformationMatrix(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
                    WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }

    private void prepareRender(CameraController camera){
        shader.start();
        shader.loadViewMatrix(camera);
        moveFactor += WAVE_SPEED * 0.016;
        shader.loadMoveFactor(moveFactor);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
    }

    private void unbind(){
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }

    public void cleanUp()
    {
        shader.cleanUp();
    }

}