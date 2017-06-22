package com.deadvikingstudios.norsetown.view.lwjgl.renderers;

import com.deadvikingstudios.norsetown.view.RenderMath;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.EntityMesh;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class EntityRenderer extends MeshRenderer
{
    public static void render(EntityMesh entity, StaticShader shader)
    {
        GL30.glBindVertexArray(entity.getMesh().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        Matrix4f transform = RenderMath.createTransformationMatrix(entity.getPosition(),
                entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
        shader.loadTransformationMatrix(transform);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
}
