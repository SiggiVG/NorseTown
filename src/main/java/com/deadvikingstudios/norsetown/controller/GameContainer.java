package com.deadvikingstudios.norsetown.controller;


import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.humanoids.EntityHumanoid;
import com.deadvikingstudios.norsetown.model.lighting.SpotLight;
import com.deadvikingstudios.norsetown.model.lighting.SunLight;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.CalendarNorse;
import com.deadvikingstudios.norsetown.model.world.Chunk;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.view.lwjgl.DisplayManager;
import com.deadvikingstudios.norsetown.view.lwjgl.Loader;
import com.deadvikingstudios.norsetown.view.lwjgl.renderers.MasterRenderer;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.ChunkMesh;
import com.deadvikingstudios.norsetown.view.meshes.EntityMesh;
import com.deadvikingstudios.norsetown.view.meshes.MeshTexture;
import com.deadvikingstudios.norsetown.view.meshes.RawMesh;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SiggiVG on 6/19/2017.
 */
public class GameContainer implements Runnable, IGameContainer
{
    public static final String GAME_NAME = "NorseTown";
    public static final String VERSION = "Indev-0.02b";

    public static final String SRC_PATH = "/src/main/java/com/deadvikingstudios/norsetown/";
    private static boolean outputFPS = false;

    public Thread thread;
    private GameContainer game;

    protected boolean isRunning = false;
    private final double UPDATE_CAP = 1.0/60.0;
    public static final int TARGET_FPS = 60;

    public static Loader loader = null;
    public static StaticShader shader = null;
    public static MasterRenderer renderer = null;

    public static final String MODE = "debug";

    private static World currentWorld;

    private static List<ChunkMesh> chunks = new ArrayList<ChunkMesh>();
    private static List<EntityMesh> entityMeshes = new ArrayList<EntityMesh>();

    private static RawMesh defaultMesh;

    private static CameraController camera;
    private static MousePicker picker;

    private static boolean renderWireFrame = false;

    //Skybox
    private static Entity skybox;
    private static RawMesh skyMesh;
    private static EntityMesh skyEntMesh;
    private static MeshTexture skyboxTexture;
    //private static SunLight curSunLight;
    private static SunLight sunLight;
    private static SpotLight spotLight;

    //Sea
    private static Entity sea;
    private static Entity seaDeep;
    private static RawMesh seaMesh;
    private static EntityMesh seaEntMesh;
    private static EntityMesh seaDeepEntMesh;
    private static MeshTexture seaTexture;
    private static MeshTexture seaDeepTexture;


    //temp
    private static Entity onion;
    private static RawMesh onionMesh;
    private static EntityMesh onionEntMesh;
    private static MeshTexture onionTexture;

    //terrain atlas
    private static MeshTexture terrainTexture;



    //private static MeshTexture entTexture;
    public CalendarNorse calendar;

    public GameContainer() {this.game = this;}

    public void start()
    {
        DisplayManager.create();

        loader = new Loader();
        shader = new StaticShader();
        renderer = new MasterRenderer(shader);

        camera = new CameraController(0, 0, 0);
        defaultMesh = loader.loadToVAO(vertices, indices, uv, cubeNormals);

        thread = new Thread(this);
        thread.run();
    }

    public void stop()
    {
        this.isRunning = false;
    }



    //TODO: make better and move to treeGen
    private static void makeTree(int x, int height, int z, int leafstart)
    {
        for (int j = Chunk.CHUNK_HEIGHT - 1; j >= 0; --j)
        {
            if(World.getWorld().getTile(x,j,z) == 2)
            {
                for (int i = 2; i < height+2; i++)
                {
                    World.getWorld().setTile(Tile.Tiles.tileLogMed, x, j+i, z, false);
                }
                return;
            }
        }
    }

    public void init()
    {
        System.out.println("Initialization started");

        Tile.Tiles.init();

        camera.setRotation(35, 135, 0);
        camera.setPosition(0, Chunk.CHUNK_HEIGHT * 0.5f * Tile.TILE_HEIGHT, 0);

        //sunLight = new SunLight(new Vector3f(0.8f,0.8f,0.8f), new Vector3f(35f,45f,0.0f), 1.0f);
        spotLight = new SpotLight(new Vector3f(0,0,0), new Vector3f(0.8f,0.8f,0.8f));

        skybox = new Entity(0,0,0,0,0,180);
        skyMesh = loader.loadToVAO(skyVertices, indices, skyboxUV, skyBoxNormals);
        skyboxTexture = new MeshTexture(loader.loadTexture("textures/skybox"));
        skyEntMesh = new EntityMesh(skybox, skyMesh, skyboxTexture);

        sea = new Entity(0,0,0,0,0,0);
        seaDeep = new Entity(0,0,0,0,0,0);
        seaMesh = loader.loadToVAO(seaVertices, seaIndices, seaUV, quadNormals);
        seaTexture = new MeshTexture(loader.loadTexture("textures/sea"));
        seaDeepTexture = new MeshTexture(loader.loadTexture("textures/seaDeep"));
        seaEntMesh = new EntityMesh(sea, seaMesh, seaTexture);
        seaDeepEntMesh = new EntityMesh(seaDeep, seaMesh, seaDeepTexture);

        terrainTexture = new MeshTexture(loader.loadTexture("textures/terrain"));//"textures/tiles/grass_top"));

        //temp
        /*onion = new Entity(0,40,0,0,0,0 );
        onionTexture = new MeshTexture(loader.loadTexture("meshes/onion"));
        onionMesh = OBJLoader.loadObjModel("meshes/onion", loader);*/

        //entTexture = new MeshTexture(loader.loadTexture("textures/entTexture"));
        calendar = new CalendarNorse();
        currentWorld = new World();

        picker = new MousePicker(camera, renderer.getProjectionMatrix(), currentWorld);

        Random random = World.getWorld().getRandom();

        for (int i = 0; i < random.nextInt(World.CHUNK_NUM_XZ * 32) + World.CHUNK_NUM_XZ*16; i++)
        {
            makeTree(random.nextInt(World.CHUNK_NUM_XZ * Chunk.CHUNK_SIZE),
                    3,//(Chunk.CHUNK_HEIGHT/32),
                    random.nextInt(World.CHUNK_NUM_XZ * Chunk.CHUNK_SIZE), 6);
        }

        for (int i = 0; i < World.CHUNK_NUM_XZ; i++)
        {
            for (int j = 0; j < World.CHUNK_NUM_Y; j++)
            {
                for (int k = 0; k < World.CHUNK_NUM_XZ; k++)
                {
                    Chunk chunk = currentWorld.getChunkAtIndex(i,j,k);
                    if(chunk != null)
                    {
                        chunk.setFlagForReMesh(false);
                        chunks.add(new ChunkMesh(chunk, terrainTexture));
                    }

                }
            }
        }

        currentWorld.getEntities().add(new EntityHumanoid(0,0,0,0,0,0));



        //temp
        //onionEntMesh = new EntityMesh(onion, onionMesh, onionTexture);


        for (Entity ent : World.getWorld().getEntities())
        {
            //entityMeshes.add(new EntityMesh(ent, defaultMesh, entTexture));
        }

        System.out.println("Initialization finished");
    }

    public void update(float dt)
    {
        calendar.update();

        KeyboardInput.update();
        MouseInput.update();

        camera.move();

        skybox.setPosition(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
        skybox.rotate(0,0,15f*24f/(float)CalendarNorse.DAY_LENGTH);
        sea.setPosition(camera.getPosition().x, 8*Tile.TILE_HEIGHT-(Tile.TILE_HEIGHT*0.3f), camera.getPosition().z);
        seaDeep.setPosition(camera.getPosition().x, 0, camera.getPosition().z);
        //sunLight.update();
        spotLight.setPosition(new Vector3f(camera.getPosition().x,camera.getPosition().y+10, camera.getPosition().z-10));

        picker.update();
        //onion.setPosition(camera.getPosition().x, camera.getPosition().y-1, camera.getPosition().z);
        //System.out.println(picker.getCurrentRay());
        //System.out.println(picker.getCurrentWorldPoint());

        currentWorld.update();

        for (ChunkMesh mesh : chunks)
        {
            if(mesh.getChunk().isFlagForReMesh())
            {
                mesh.reloadMesh();
                mesh.getChunk().setFlagForReMesh(false);
            }
        }

        if(MouseInput.getButtonDown(0))
        {
            System.out.println("Time: " + (calendar.getTime()/(float)CalendarNorse.DAY_LENGTH) * 24f);
        }

        //Move to World.update()
        for (Entity ent : World.getWorld().getEntities())
        {
            ent.update();
        }

        if (KeyboardInput.getKeyDown(Keyboard.KEY_F1))
        {
            if(renderWireFrame)
            {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                //GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            else
            {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                //GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            renderWireFrame = !renderWireFrame;
        }

        //TODO remove this
        if (KeyboardInput.getKeyDown(Keyboard.KEY_1))
        {
            World.chunkTickSpeed = 4;
        }
        else if (KeyboardInput.getKeyDown(Keyboard.KEY_2))
        {
            World.chunkTickSpeed = 8;
        }
        else if (KeyboardInput.getKeyDown(Keyboard.KEY_3))
        {
            World.chunkTickSpeed = 16;
        }
        else if (KeyboardInput.getKeyDown(Keyboard.KEY_4))
        {
            World.chunkTickSpeed = 32;
        }
        else if (KeyboardInput.getKeyDown(Keyboard.KEY_5))
        {
            World.chunkTickSpeed = 64;
        }/*
        else if (KeyboardInput.getKeyDown(Keyboard.KEY_6))
        {
            World.chunkTickSpeed = 128;
        }
        else if (KeyboardInput.getKeyDown(Keyboard.KEY_7))
        {
            World.chunkTickSpeed = 256;
        }
        else if (KeyboardInput.getKeyDown(Keyboard.KEY_8))
        {
            World.chunkTickSpeed = 512;
        }*/
    }
    private static Vector3f ambientLight = new Vector3f(0.3f,0.3f,0.3f);

    public void render()
    {
        //Display is cleared before drawing
        renderer.clear();
        //start rendering
        //ent.translate(0.001f, 0.001f, 0);
        //ent.scale(-0.001f);
        //ent.rotate(0f, 0f,0.1f);
        shader.start();
        shader.loadAmbientLight(ambientLight);
        //shader.loadDirectionalLight(sunLight);
        shader.loadSpotLight(spotLight);
        shader.loadViewMatrix(camera);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        renderer.render(skyEntMesh, shader);

        GL11.glEnable(GL11.GL_DEPTH_TEST);


        //shader.loadTime(calendar.getTime()/CalendarNorse.DAY_LENGTH);

        for (ChunkMesh chunk : chunks)
        {
            renderer.render(chunk, shader);
        }
        for (EntityMesh mesh : entityMeshes)
        {
            renderer.render(mesh, shader);
            //System.out.println("CameraController: " + CameraController.getChunkPosition());
            //System.out.println(mesh.getEntity().getChunkPosition());
        }

        //renderer.render(onionEntMesh, shader);

        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        renderer.render(seaDeepEntMesh, shader);
        renderer.render(seaEntMesh, shader);
        //GL11.glEnable(GL11.GL_DEPTH_TEST);

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
        System.setProperty("org.lwjgl.librarypath", new File("libs" + File.separator + "native" + File.separator + OperatingSystem.getOSforLWJGLNatives()).getAbsolutePath());
        new GameContainer().start();
    }

    public static RawMesh getDefaultMesh()
    {
        return defaultMesh;
    }

    private static float[] vertices = {
            0,1,1,
            0,0,1,
            1,0,1,
            1,1,1,

            1,1,1,
            1,0,1,
            1,0,0,
            1,1,0,

            1,1,0,
            1,0,0,
            0,0,0,
            0,1,0,

            0,1,0,
            0,0,0,
            0,0,1,
            0,1,1,

            1,1,1,
            1,1,0,
            0,1,0,
            0,1,1,

            0,0,1,
            0,0,0,
            1,0,0,
            1,0,1
    };

    private static float[] cubeNormals =
    {
            0,0,1,
            0,0,1,
            0,0,1,
            0,0,1,

            1,0,0,
            1,0,0,
            1,0,0,
            1,0,0,

            0,0,-1,
            0,0,-1,
            0,0,-1,
            0,0,-1,

            -1,0,0,
            -1,0,0,
            -1,0,0,
            -1,0,0,

            0,1,0,
            0,1,0,
            0,1,0,
            0,1,0,

            0,-1,0,
            0,-1,0,
            0,-1,0,
            0,-1,0,
    };

    private static float[] skyBoxNormals =
            {
                    0,0,-1,
                    0,0,-1,
                    0,0,-1,
                    0,0,-1,

                    -1,0,0,
                    -1,0,0,
                    -1,0,0,
                    -1,0,0,

                    0,0,1,
                    0,0,1,
                    0,0,1,
                    0,0,1,

                    1,0,0,
                    1,0,0,
                    1,0,0,
                    1,0,0,

                    0,-1,0,
                    0,-1,0,
                    0,-1,0,
                    0,-1,0,

                    0,1,0,
                    0,1,0,
                    0,1,0,
                    0,1,0,

                };

    private static float[] quadNormals =
    {
            0,1,0,
            0,1,0,
            0,1,0,
            0,1,0,
    };


    private static float skyboxSize = 10;//(float)((MasterRenderer.P_FAR_PLANE-10f)/(Math.sqrt(3)));
    private static float[] skyVertices = {
            skyboxSize,skyboxSize,skyboxSize,
            skyboxSize,-skyboxSize,skyboxSize,
            -skyboxSize,-skyboxSize,skyboxSize,
            -skyboxSize,skyboxSize,skyboxSize,

            skyboxSize,skyboxSize,-skyboxSize,
            skyboxSize,-skyboxSize,-skyboxSize,
            skyboxSize,-skyboxSize,skyboxSize,
            skyboxSize,skyboxSize,skyboxSize,

            -skyboxSize,skyboxSize,-skyboxSize,
            -skyboxSize,-skyboxSize,-skyboxSize,
            skyboxSize,-skyboxSize,-skyboxSize,
            skyboxSize,skyboxSize,-skyboxSize,

            -skyboxSize,skyboxSize,skyboxSize,
            -skyboxSize,-skyboxSize,skyboxSize,
            -skyboxSize,-skyboxSize,-skyboxSize,
            -skyboxSize,skyboxSize,-skyboxSize,

            -skyboxSize,skyboxSize,skyboxSize,
            -skyboxSize,skyboxSize,-skyboxSize,
            skyboxSize,skyboxSize,-skyboxSize,
            skyboxSize,skyboxSize,skyboxSize,

            skyboxSize,-skyboxSize,skyboxSize,
            skyboxSize,-skyboxSize,-skyboxSize,
            -skyboxSize,-skyboxSize,-skyboxSize,
            -skyboxSize,-skyboxSize,skyboxSize
    };

    private static float[] skyboxUV = {
            0.5f,0,
            0.5f,0.5f,
            1f,0.5f,
            1f,0,

            1f,0,
            1f,0.5f,
            0.5f,0.5f,
            0.5f,0,

            0.5f,0,
            0.5f,0.5f,
            1f,0.5f,
            1f,0,

            1f,0,
            1f,0.5f,
            0.5f,0.5f,
            0.5f,0,

            0,0,
            0,0.5f,
            0.5f,0.5f,
            0.5f,0,

            0.5f,0.5f,
            0.5f,1f,
            1f,1f,
            1f,0.5f,
    };

    private static float[] seaVertices =
            {
                    250,0,250,
                    250,0,-250,
                    -250,0,-250,
                    -250,0,250
            };

    private static int[] seaIndices = {
            0, 1, 3,
            3, 1, 2
    };

    private static float[] seaUV = {
            0, 0,
            0, 1,
            1, 1,
            1, 0
    };

    private static int[] indices = {
            0, 1, 3,
            3, 1, 2,

            4, 5, 7,
            7, 5, 6,

            8, 9, 11,
            11, 9, 10,

            12, 13, 15,
            15, 13, 14,

            16, 17, 19,
            19, 17, 18,

            20, 21, 23,
            23, 21, 22
    };

    private static float[] uv = {
            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0,

            0,0,
            0,1,
            1,1,
            1,0
    };

    @Override
    public void run()
    {
        isRunning = true;

        boolean render = false;
        double firstTime;
        double lastTime = System.nanoTime() / 1_000_000_000.0;
        double passedTime;
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


                //FPS output
                if(MODE.equals("debug"))
                {
                    if (frameTime >= 1.0)
                    {
                        frameTime = 0;
                        fps = frames;
                        frames = 0;
                    }
                }
            }

            if(render)
            {
                renderer.clear();
                game.render();

                if(MODE.equals("debug") && outputFPS)
                {
                    //renderer.drawText("FPS: " + fps, 0, 0, 0xff_00_ff_ff);
                    System.out.println("FPS: " + fps);
                }

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
}

