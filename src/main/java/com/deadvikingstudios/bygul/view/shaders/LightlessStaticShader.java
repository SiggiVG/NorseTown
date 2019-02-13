package com.deadvikingstudios.bygul.view.shaders;

public class LightlessStaticShader extends StaticShader
{
    private static final String VERTEX_FILE_LIGHTLESS = /*GameContainer.RES_PATH + */"/bygul/shaders/vertex_lightless.vert";
    private static final String FRAGMENT_FILE_LIGHTLESS = /*GameContainer.RES_PATH + */"/bygul/shaders/fragment_lightless.frag";

    public LightlessStaticShader()
    {
        super(VERTEX_FILE_LIGHTLESS, FRAGMENT_FILE_LIGHTLESS);
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uvs");
        //super.bindAttribute(2,"normals");
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        /*location_lightPosition = super.getUniformLocation("lightPosition");
        location_spotLightColor = super.getUniformLocation("spotLightColor");
        location_ambientLight = super.getUniformLocation("ambientLight");
        location_directionalLightColor = super.getUniformLocation("directionalLight.color");
        location_directionalLightDirection = super.getUniformLocation("directionalLight.direction");
        location_directionalLightIntensity = super.getUniformLocation("directionalLight.intensity");*/
    }
}
