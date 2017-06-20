package com.deadvikingstudios.norsetown.view.lwjgl;

import com.deadvikingstudios.norsetown.view.lwjgl.models.TexturedModel;
import org.lwjgl.opengl.GL11;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class MasterRenderer
{
    public void prepare()
    {
        GL11.glClearColor(0.4f, 0.7f, 1.0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void render(TexturedModel model)
    {
        ModelRenderer.render(model);
    }
}
