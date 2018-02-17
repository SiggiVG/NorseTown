package com.deadvikingstudios.norsetown.view.renderers;

import com.deadvikingstudios.norsetown.controller.CameraController;
import com.deadvikingstudios.norsetown.model.world.WaterTile;
import com.deadvikingstudios.norsetown.utils.RenderMath;
import com.deadvikingstudios.norsetown.view.Loader;
import com.deadvikingstudios.norsetown.view.shaders.WaterShader;
import com.deadvikingstudios.norsetown.view.meshes.RawMesh;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class RendererWater {

    private RawMesh quad;
    private WaterShader shader;

    public RendererWater(Loader loader, WaterShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }

    public void render(List<WaterTile> water, CameraController camera) {
        prepareRender(camera);
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = RenderMath.createTransformationMatrix(
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
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
    }

    private void unbind(){
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices);
    }

    public void cleanUp()
    {
        shader.cleanUp();
    }

}