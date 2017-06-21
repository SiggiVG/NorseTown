package com.deadvikingstudios.norsetown.view.lwjgl.renderers;

import com.deadvikingstudios.norsetown.view.meshes.TexturedMesh;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * Renders Objects through LightWeightJavaGraphicsLibrary(OpenGL)
 */
public class MeshRenderer
{
    public static void render(TexturedMesh model)
    {
        GL30.glBindVertexArray(model.getMesh().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

}
