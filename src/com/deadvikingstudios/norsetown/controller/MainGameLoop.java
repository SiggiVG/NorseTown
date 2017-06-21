package com.deadvikingstudios.norsetown.controller;

import com.deadvikingstudios.norsetown.model.entities.Camera;
import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.world.Chunk;
import com.deadvikingstudios.norsetown.view.lwjgl.DisplayManager;
import com.deadvikingstudios.norsetown.view.lwjgl.Loader;
import com.deadvikingstudios.norsetown.view.lwjgl.renderers.MasterRenderer;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.ChunkMesh;
import com.deadvikingstudios.norsetown.view.meshes.EntityMesh;
import com.deadvikingstudios.norsetown.view.meshes.MeshTexture;
import com.deadvikingstudios.norsetown.view.meshes.RawMesh;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class MainGameLoop
{
    public static final String GAME_NAME = "NorseTown";
    public static final String VERSION = "Indev-0.01a";

    public static final String SRC_PATH = "src/com/deadvikingstudios/norsetown/";
    public static final String RES_PATH = "res/";

    public static Loader loader = null;
    public static StaticShader shader = null;
    public static MasterRenderer renderer = null;

    private static List<ChunkMesh> chunks = new ArrayList<ChunkMesh>();
    private static List<Vector3f> usedPos = new ArrayList<Vector3f>();

    public static Camera camera;
    private static Vector3f camPos = new Vector3f(0,0,0);

    public static void main(String[] args)
    {
        DisplayManager.createDisplay();
        loader = new Loader();
        shader = new StaticShader();
        renderer = new MasterRenderer(shader);

        //RawMesh mesh = loader.loadToVAO(vertices, indices, uvs);
        MeshTexture texture= new MeshTexture(loader.loadTexture("textures/tiles/grass_top"));
        //EntityMesh entity = new EntityMesh(new Entity(0,2,0), mesh, texture);
        
        //ent.setRotationZ(45f);//iso angle
        //ent.setRotationX(-35.264f);//iso angle

        camera = new Camera(new Vector3f(-2,0,+1), 0, 0, 0);

        while(!Display.isCloseRequested())
        {
            //Game Logic
            update();

            //TODO generate cubes via chunks
            for (int i = 0; i < 1; i+=Chunk.CHUNK_SIZE)
            {
                for (int j = 0; j < 1; j+=Chunk.CHUNK_SIZE)
                {
                    if(!usedPos.contains(new Vector3f(i,0,j)))
                    {
                        chunks.add(new ChunkMesh(new Chunk(i,0,j), texture));
                        usedPos.add(new Vector3f(i,0,j));
                    }
                }
            }

            render();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static void update()
    {
        camera.move();
        camPos = camera.getPosition();
    }

    private static void render()
    {
        //Display is cleared before drawing
        renderer.prepare();
        //start rendering

        //ent.translate(0.001f, 0.001f, 0);
        //ent.scale(-0.001f);
        //ent.rotate(0f, 0f,0.1f);
        shader.start();
        shader.loadViewMatrix(camera);

        for (ChunkMesh chunk : chunks)
        {
            renderer.render(chunk, shader);
        }

        //renderer.render(mesh, shader);
        shader.stop();
        //stop rendering
        //Displays to screen
        DisplayManager.updateDisplay();
    }
}
