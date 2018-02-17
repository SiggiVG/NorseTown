package com.deadvikingstudios.norsetown.view.renderers;

import com.deadvikingstudios.norsetown.controller.CameraController;
import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.view.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.EntityMesh;
import com.deadvikingstudios.norsetown.view.meshes.StructureMesh;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class LightlessRenderer extends Renderer
{

    public LightlessRenderer(StaticShader shader)
    {
        super(shader);
    }

    public void renderScene(List<EntityMesh> entities, List<StructureMesh> structures, CameraController camera)
    {
        shader.start();

        shader.loadViewMatrix(camera);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        render(GameContainer.skyEntMesh, this.shader);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        //this.render(seaDeepEntMesh, lightlessShader);

        shader.stop();
    }
}
