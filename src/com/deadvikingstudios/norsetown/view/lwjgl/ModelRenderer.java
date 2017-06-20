package com.deadvikingstudios.norsetown.view.lwjgl;

import com.deadvikingstudios.norsetown.view.lwjgl.models.RawModel;
import com.deadvikingstudios.norsetown.view.lwjgl.models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * Renders Objects through LightWeightJavaGraphicsLibrary(OpenGL)
 */
public class ModelRenderer //Entity Renderer
{
    public static void render(TexturedModel texturedModel)
    {
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

}
