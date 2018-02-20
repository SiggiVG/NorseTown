package com.deadvikingstudios.norsetown.view.guis;

import com.deadvikingstudios.norsetown.utils.RenderMath;
import com.deadvikingstudios.norsetown.view.Loader;
import com.deadvikingstudios.norsetown.view.meshes.RawMesh;
import com.deadvikingstudios.norsetown.view.shaders.GuiShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

public class GuiRenderer
{
    private final RawMesh quad;
    private GuiShader shader;

    public GuiRenderer(Loader loader)
    {
        float[] positions = {
            -1,1,
            -1,-1,
            1,1,
            1,-1
        };
        quad = loader.loadToVAO(positions, 2);
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guis)
    {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        //render
        for(GuiTexture gui : guis)
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
            Matrix4f matrix = RenderMath.createTransformationMatrix(gui.getPosition(), gui.getScale());
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp()
    {
        shader.cleanUp();
    }
}
