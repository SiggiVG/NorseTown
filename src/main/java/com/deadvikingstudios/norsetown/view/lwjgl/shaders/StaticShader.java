package com.deadvikingstudios.norsetown.view.lwjgl.shaders;

import com.deadvikingstudios.norsetown.view.RenderMath;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class StaticShader extends ShaderProgram
{
    private static final String VERTEX_FILE = /*GameContainer.RES_PATH + */"/shaders/basic_chunk_shader.vert";
    private static final String FRAGMENT_FILE = /*GameContainer.RES_PATH + */"/shaders/basic_chunk_shader.frag";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uvs");
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    /**
     * A Transform contains:
     * Translation (x,y,z)
     * Rotation (rx,ry,rz) //Euler Angles
     * Scale (s,s,s)
     *
     * We can also use a 4x4 Transformation Matrix
     *
     * @param matrix
     */
    public void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadTransformationMatrix(Vector3f translate, float rx, float ry, float rz, float scale)
    {
        this.loadTransformationMatrix(RenderMath.createTransformationMatrix(translate, rx, ry, rz, scale));
    }

    public void loadProjectionMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix()
    {
        super.loadMatrix(location_viewMatrix, RenderMath.createViewMatrix());
    }
}
