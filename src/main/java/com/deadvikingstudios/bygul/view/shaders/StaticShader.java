package com.deadvikingstudios.bygul.view.shaders;

import com.deadvikingstudios.bygul.controller.CameraController;
import com.deadvikingstudios.bygul.model.lighting.DirectionalLight;
import com.deadvikingstudios.bygul.model.lighting.SpotLight;
import com.deadvikingstudios.bygul.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class StaticShader extends ShaderProgram
{
    private static final String VERTEX_FILE = /*GameContainer.RES_PATH + */"/bygul/shaders/vertex.vert";
    private static final String FRAGMENT_FILE = /*GameContainer.RES_PATH + */"/bygul/shaders/fragment.frag";

    //matrices
    protected int location_transformationMatrix;
    protected int location_projectionMatrix;
    protected int location_viewMatrix;

    //Lighting
    private int location_ambientLight;

    //spot light
    private int location_spotLightPosition;
    private int location_spotLightColor;
    private int location_spotLightIntensity;

    //directional light
    private int location_directionalLightColor;
    private int location_directionalLightDirection;
    private int location_directionalLightIntensity;

    //for specular lighting
    private int location_shineDamper;
    private int location_reflectivity;

    //Clipping Planes
    private int location_plane;


    public StaticShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
    protected StaticShader(String vertexPath, String fragmentPath)
    {
        super(vertexPath, fragmentPath);
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
        //lighting
        location_shineDamper = super.getUniformLocation("meshSpecularProps.shineDamper");
        location_reflectivity = super.getUniformLocation("meshSpecularProps.reflectivity");

        location_ambientLight = super.getUniformLocation("ambientLight");

        location_spotLightColor = super.getUniformLocation("spotLight.color");
        location_spotLightPosition = super.getUniformLocation("spotLight.position");
        location_spotLightIntensity = super.getUniformLocation("spotLight.intensity");

        location_directionalLightColor = super.getUniformLocation("directionalLight.color");
        location_directionalLightDirection = super.getUniformLocation("directionalLight.direction");
        location_directionalLightIntensity = super.getUniformLocation("directionalLight.intensity");

        location_plane = super.getUniformLocation("plane");
    }

    /**
     * A Transform contains:
     * Translation (x,y,y)
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
        this.loadTransformationMatrix(Maths.createTransformationMatrix(translate, rx, ry, rz, scale));
    }

    public void loadProjectionMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(CameraController camera)
    {
        super.loadMatrix(location_viewMatrix, Maths.createViewMatrix(camera));
    }

    public void loadDirectionalLight(DirectionalLight light)
    {
        super.loadVector3D(location_directionalLightColor, light.getColor());
        super.loadVector3D(location_directionalLightDirection, light.getDirection());
        super.loadFloat(location_directionalLightIntensity, light.getIntensity());
    }

    public void loadSpotLight(SpotLight light)
    {
        super.loadVector3D(location_spotLightColor, light.getColor());
        super.loadVector3D(location_spotLightPosition, light.getPosition());
        super.loadFloat(location_spotLightIntensity, light.getIntensity());
    }

    public void loadAmbientLight(Vector3f lightColor)
    {
        super.loadVector3D(location_ambientLight, lightColor);
    }

    //for specular lighting
    public void loadShineVariables(float damper, float reflectivity)
    {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadClipPlane(Vector4f vec)
    {
        super.loadVector4D(location_plane, vec);
    }
}
