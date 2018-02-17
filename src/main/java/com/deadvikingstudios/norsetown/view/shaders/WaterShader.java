package com.deadvikingstudios.norsetown.view.shaders;

import com.deadvikingstudios.norsetown.controller.CameraController;
import com.deadvikingstudios.norsetown.utils.RenderMath;
import org.lwjgl.util.vector.Matrix4f;

public class WaterShader extends ShaderProgram {

    private final static String VERTEX_FILE = "/shaders/vertex_water.vert";
    private final static String FRAGMENT_FILE = "/shaders/fragment_water.frag";

    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;

    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(CameraController camera){
        Matrix4f viewMatrix = RenderMath.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }

}