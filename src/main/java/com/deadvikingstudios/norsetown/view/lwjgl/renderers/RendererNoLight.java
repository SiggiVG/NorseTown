package com.deadvikingstudios.norsetown.view.lwjgl.renderers;

import com.deadvikingstudios.norsetown.controller.CameraController;
import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.EntityMesh;
import com.deadvikingstudios.norsetown.view.meshes.StructureMesh;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RendererNoLight extends Renderer
{

    public RendererNoLight(StaticShader shader)
    {
        super(shader);
    }

    @Override
    public void renderScene(List<EntityMesh> entities, List<StructureMesh> structures, CameraController camera)
    {
        shader.start();

        shader.loadViewMatrix(camera);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        render(GameContainer.skyEntMesh, GameContainer.shaderNoLight);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        //this.render(seaDeepEntMesh, shaderNoLight);

        shader.stop();
    }
}
