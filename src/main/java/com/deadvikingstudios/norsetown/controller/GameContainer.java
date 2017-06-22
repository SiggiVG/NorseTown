package com.deadvikingstudios.norsetown.controller;


import com.deadvikingstudios.norsetown.view.Camera;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.view.lwjgl.DisplayManager;
import com.deadvikingstudios.norsetown.view.lwjgl.Loader;
import com.deadvikingstudios.norsetown.view.lwjgl.renderers.MasterRenderer;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.ChunkMesh;
import com.deadvikingstudios.norsetown.view.meshes.MeshTexture;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class GameContainer implements Runnable, IGameContainer
{
    public static final String GAME_NAME = "NorseTown";
    public static final String VERSION = "Indev-0.01a";

    public static final String SRC_PATH = "/src/main/java/com/deadvikingstudios/norsetown/";
    //public static final String RES_PATH = "res/";

    public Thread thread;
    private GameContainer game;

    protected boolean isRunning = false;
    private final double UPDATE_CAP = 1.0/60.0;
    public static final int TARGET_FPS = 60;

    public static Loader loader = null;
    public static StaticShader shader = null;
    public static MasterRenderer renderer = null;

    private static World currentWorld;

    private static List<ChunkMesh> chunks = new ArrayList<ChunkMesh>();

    private static MeshTexture grassTexture;

    public GameContainer() {this.game = this;}

    public void start()
    {
        DisplayManager.create();

        loader = new Loader();
        shader = new StaticShader();
        renderer = new MasterRenderer(shader);

        thread = new Thread(this);
        thread.run();
    }

    public void stop()
    {
        this.isRunning = false;
    }

    @Override
    public void run()
    {
        isRunning = true;

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1_000_000_000.0;
        double passedTime = 0;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        game.init();

        while(isRunning && !Display.isCloseRequested())
        {
            firstTime = System.nanoTime() / 1_000_000_000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while(unprocessedTime >= UPDATE_CAP)
            {
                unprocessedTime -= UPDATE_CAP;
                render = true;

                game.update((float) UPDATE_CAP);
                //input.update();

                //FPS output
                /*if(MODE.equals("debug"))
                {
                    if (frameTime >= 1.0)
                    {
                        frameTime = 0;
                        fps = frames;
                        frames = 0;
                    }
                }*/
            }

            if(render)
            {
                renderer.clear();
                game.render();
                //renderer.process();
                /*if(MODE.equals("debug"))
                {
                    renderer.drawText("FPS: " + fps, 0, 0, 0xff_00_ff_ff);
                }*/
                //System.out.println("FPS: " + fps);
                //window.update();
                DisplayManager.resize();
                frames++;
            }
            else
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        dispose();
    }
    /*


        Camera.create();

        //RawMesh mesh = loader.loadToVAO(vertices, indices, uvs);
        grassTexture = new MeshTexture(loader.loadTexture("textures/tiles/grass_top"));
        //EntityMesh entity = new EntityMesh(new Entity(0,2,0), mesh, texture);

        //ent.setRotationZ(45f);//iso angle
        //ent.setRotationX(-35.264f);//iso angle



        currentWorld = new World();

        for (int i = 0; i < World.CHUNK_NUM_XZ; i++)
        {
            for (int j = 0; j < World.CHUNK_NUM_Y; j++)
            {
                for (int k = 0; k < World.CHUNK_NUM_XZ; k++)
                {
                    chunks.add(new ChunkMesh(currentWorld.getChunk(i,j,k), grassTexture));
                }
            }
        }





        /*new Thread(new Runnable() //I cant do openGL stuff inside of a thread?
        {
            @Override
            public void run()
            {
                while(!Display.isCloseRequested())
                {
                    for (int x = 0; x < 3; x++)
                    {
                        for (int z = 0; z < 3; z++)
                        {
                            if (!usedPos.contains(new Vector3f(x, 0, z)))
                            {

                                usedPos.add(new Vector3f(x, 0, z));
                            }
                        }
                        for (int z = -3; z < 0; z++)
                        {
                            if (!usedPos.contains(new Vector3f(x, 0, z)))
                            {
                                chunks.add(new ChunkMesh(new Chunk(x*Chunk.CHUNK_SIZE, 0, z*Chunk.CHUNK_SIZE), grassTexture));
                                usedPos.add(new Vector3f(x, 0, z));
                            }
                        }
                    }
                }
            }
        }).start();

        DisplayManager.resize();

        running = true;

        while(running && !Display.isCloseRequested())
        {
            //

            //Game Logic
            update();

            //rendering
            render();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.dispose();
    }*/
    public void init()
    {
        Camera.create();

        grassTexture = new MeshTexture(loader.loadTexture("textures/tiles/grass_top"));
        currentWorld = new World();

        for (int i = 0; i < World.CHUNK_NUM_XZ; i++)
        {
            for (int j = 0; j < World.CHUNK_NUM_Y; j++)
            {
                for (int k = 0; k < World.CHUNK_NUM_XZ; k++)
                {
                    chunks.add(new ChunkMesh(currentWorld.getChunk(i,j,k), grassTexture));
                }
            }
        }
    }


    public void update(float dt)
    {
        Camera.acceptInput(dt);
        Camera.apply();
    }

    public void render()
    {
        //Display is cleared before drawing
        renderer.clear();
        //start rendering

        //ent.translate(0.001f, 0.001f, 0);
        //ent.scale(-0.001f);
        //ent.rotate(0f, 0f,0.1f);
        shader.start();
        shader.loadViewMatrix();

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

    public void dispose()
    {
        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.dispose();
    }

    public static void main(String[] args)
    {
        String fileNatives = OperatingSystem.getOSforLWJGLNatives();
        //System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + File.separator + "libs" + File.separator + "native" + File.separator + fileNatives);
        System.setProperty("org.lwjgl.librarypath", new File("libs/native" + File.separator + fileNatives).getAbsolutePath());
        //System.err.println(System.getProperty("java.library.path"));
        //System.setProperty("java.library.path", new File("libs/native" + File.separator + fileNatives).getAbsolutePath());
        new GameContainer().start();
    }
}
