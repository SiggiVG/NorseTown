package com.deadvikingstudios.norsetown.view.lwjgl.shaders;

import com.deadvikingstudios.norsetown.controller.CameraController;
import com.deadvikingstudios.norsetown.model.lighting.DirectionalLight;
import com.deadvikingstudios.norsetown.model.lighting.SpotLight;
import com.deadvikingstudios.norsetown.view.RenderMath;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class StaticShader extends ShaderProgram
{
    private static final String VERTEX_FILE = /*GameContainer.RES_PATH + */"/shaders/vertex.vs";
    private static final String FRAGMENT_FILE = /*GameContainer.RES_PATH + */"/shaders/fragment.fs";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_spotLightColor;
    private int location_ambientLight;
    private int location_directionalLightColor;
    private int location_directionalLightDirection;
    private int location_directionalLightIntensity;

    public StaticShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uvs");
        super.bindAttribute(2,"normals");
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_spotLightColor = super.getUniformLocation("spotLightColor");
        location_ambientLight = super.getUniformLocation("ambientLight");
        location_directionalLightColor = super.getUniformLocation("directionalLight.color");
        location_directionalLightDirection = super.getUniformLocation("directionalLight.direction");
        location_directionalLightIntensity = super.getUniformLocation("directionalLight.intensity");
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

    public void loadViewMatrix(CameraController camera)
    {
        super.loadMatrix(location_viewMatrix, RenderMath.createViewMatrix(camera));
    }

    public void loadDirectionalLight(DirectionalLight light)
    {
        super.loadVector3D(location_directionalLightColor, light.getColor());
        super.loadVector3D(location_directionalLightDirection, light.getDirection());
        super.loadFloat(location_directionalLightIntensity, light.getIntensity());
    }

    public void loadSpotLight(SpotLight light)
    {
        super.loadVector3D(location_lightPosition, light.getPosition());
        super.loadVector3D(location_spotLightColor, light.getColor());
    }

    public void loadAmbientLight(Vector3f lightColor)
    {
        super.loadVector3D(location_ambientLight, lightColor);
    }
}
