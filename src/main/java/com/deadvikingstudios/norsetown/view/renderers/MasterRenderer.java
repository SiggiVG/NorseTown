package com.deadvikingstudios.norsetown.view.renderers;

import com.deadvikingstudios.norsetown.controller.CameraController;
import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.lighting.DirectionalLight;
import com.deadvikingstudios.norsetown.view.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.TexturedMesh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer
{
    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedMesh,List<Entity>> entities = new HashMap<TexturedMesh, List<Entity>>();

    public void render(DirectionalLight sunlight, CameraController camera)
    {
        //renderer.prepare()
        shader.start();
        shader.loadDirectionalLight(sunlight);
        shader.loadViewMatrix(camera);
        shader.stop();
        entities.clear();
    }

    public void cleadUp()
    {
        shader.cleanUp();
    }
}
