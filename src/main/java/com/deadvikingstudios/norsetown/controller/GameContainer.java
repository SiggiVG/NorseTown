package com.deadvikingstudios.norsetown.controller;


import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityStructure;
import com.deadvikingstudios.norsetown.model.lighting.DirectionalLight;
import com.deadvikingstudios.norsetown.model.lighting.SpotLight;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.CalendarNorse;
import com.deadvikingstudios.norsetown.model.world.WorldOld;
import com.deadvikingstudios.norsetown.model.world.structures.ChunkColumn;
import com.deadvikingstudios.norsetown.model.world.structures.StructureIsland;
import com.deadvikingstudios.norsetown.model.world.structures.StructureTree;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import com.deadvikingstudios.norsetown.view.lwjgl.Loader;
import com.deadvikingstudios.norsetown.view.lwjgl.WindowManager;
import com.deadvikingstudios.norsetown.view.lwjgl.renderers.Renderer;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.LightlessStaticShader;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 *
 */
public class GameContainer implements Runnable, IGameContainer
{
    public static final String GAME_NAME = "NorseTown";
    public static final String VERSION = "Indev-0.02b";

    public static final String SRC_PATH = "/src/main/java/com/deadvikingstudios/norsetown/";
    private static boolean outputFPS = false;

    private Thread thread;
    private GameContainer game;

    protected boolean isRunning = false;
    public static final int TARGET_FPS = 60;
    private final double UPDATE_CAP = 1.0/(double)TARGET_FPS;

    public static Loader loader = null;
    public static StaticShader shader = null;
    public static StaticShader shaderNoLight = null;
    public static Renderer renderer = null;
    public static Renderer rendererNoLight = null;

    public static final String DEBUG = "debug";
    public static String MODE = DEBUG;

    //private static WorldOld currentWorld;

    //private static List<ChunkMesh> chunks = new ArrayList<ChunkMesh>();
    private static List<EntityStructure> structures = new ArrayList<EntityStructure>();
    private static List<StructureMesh> structuresMeshes = new ArrayList<StructureMesh>();
    //private static List<EntityMesh> entityMeshes = new ArrayList<EntityMesh>();

    private static RawMesh defaultMesh;

    private static CameraController camera;
    //private static MousePicker picker;

    private static boolean renderWireFrame = false;

    //Skybox
    private static Entity skybox;
    private static RawMesh skyMesh;
    private static EntityMesh skyEntMesh;
    private static MeshTexture skyboxTexture;
    //private static SunLight curSunLight;
    private static Vector3f ambientLight = new Vector3f(0.4f,0.4f,0.4f);
    private static DirectionalLight sunLight;
    private static SpotLight spotLight;

    //Sea
    private static Entity sea;
    private static Entity seaDeep;
    private static RawMesh seaMesh;
    private static EntityMesh seaEntMesh;
    private static EntityMesh seaDeepEntMesh;
    private static MeshTexture seaTexture;
    private static MeshTexture seaDeepTexture;

    //terrain atlas
    public static MeshTexture terrainTexture;

    private static MeshTexture entTexture;

    public CalendarNorse calendar;

    public GameContainer() {this.game = this;}

    public void start()
    {
        WindowManager.create();

        loader = new Loader();
        shader = new StaticShader();
        shaderNoLight = new LightlessStaticShader();
        renderer = new Renderer(shader);
        rendererNoLight = new Renderer(shaderNoLight);

        thread = new Thread(this);
        thread.run();
    }

    public void stop()
    {
        this.isRunning = false;
    }



//    //TODO: make better and move to treeGen
//    private static void makeTree(int x, int height, int y, int leafstart)
//    {
//        for (int j = 16*8- 1; j >= 0; --j)
//        {
//            if(WorldOld.getWorld().getTile(x,j,y) == 2)
//            {
//                if(j > WorldOld.SEA_LEVEL + WorldOld.BEACH_HEIGHT)
//                {
//                    for (int i = 2; i < height + 2; i++)
//                    {
//                        WorldOld.getWorld().setTile(Tile.Tiles.tileTrunkFir, x, j + i, y, false);
//                    }
//                    return;
//                }
//            }
//        }
//    }

    double initStartTime;

    @Override
    public void init()
    {
        Logger.info("Initialization started");
        initStartTime = System.currentTimeMillis();

        Tile.Tiles.init();

        defaultMesh = loader.loadToVAO(vertices, indices, uv, cubeNormals);

        camera = new CameraController(0, 0, 0);
        //camera.setRotation(35, 135, 0);//135
        camera.setPosition(5, 5, -34);//256 * 0.5f * Tile.TILE_HEIGHT

        //TODO: have sun move
        sunLight = new DirectionalLight(new Vector3f(1f,1f,1f), new Vector3f(0.6f,-1f,0.2f), 1.0f);
        spotLight = new SpotLight(new Vector3f(0.8f,0.8f,1f), new Vector3f(0,0,0), 1.0f);

        skybox = new Entity(0,0,0,0,0,180);
        skyMesh = loader.loadToVAO(skyVertices, indices, skyboxUV, skyBoxNormals);
        skyboxTexture = new MeshTexture(loader.loadTexture("textures/skybox"));
        skyEntMesh = new EntityMesh(skybox, skyMesh, skyboxTexture);

        sea = new Entity(0,0,0,0,0,0);
        seaDeep = new Entity(0,0,0,0,0,0);
        seaMesh = loader.loadToVAO(seaVertices, seaIndices, seaUV, quadNormals);
        seaTexture = new MeshTexture(loader.loadTexture("textures/sea"));
        seaDeepTexture = new MeshTexture(loader.loadTexture("textures/seaDeep"));
        seaEntMesh = new EntityMesh(sea, seaMesh, seaTexture, 0.0f, 0.5f);
        seaDeepEntMesh = new EntityMesh(seaDeep, seaMesh, seaDeepTexture);

        terrainTexture = new MeshTexture(loader.loadTexture("textures/terrain"));//"textures/tiles/grass_top"));

        entTexture = new MeshTexture(loader.loadTexture("textures/entTexture"));
        calendar = new CalendarNorse();
        //currentWorld = new WorldOld();
        //System.out.println(currentWorld.getNumberOfNonEmptyChunks());
        //System.out.println(currentWorld.getMaxPossibleNumberOfChunks());

        //picker = new MousePicker(camera, renderer.getProjectionMatrix(), currentWorld);

        //temp TODO: create a terrain Generator and Decorator
        //Random random = WorldOld.getWorld().getRandom();

//        for (int i = 0; i < random.nextInt(WorldOld.CHUNK_NUM_XZ * 32) + WorldOld.CHUNK_NUM_XZ*16; i++)
//        {
//            makeTree(random.nextInt(WorldOld.CHUNK_NUM_XZ * Chunk.CHUNK_SIZE),
//                    3,//(Chunk.CHUNK_HEIGHT/32),
//                    random.nextInt(WorldOld.CHUNK_NUM_XZ * Chunk.CHUNK_SIZE), 6);
//        }
//
//        for (int i = 0; i < WorldOld.CHUNK_NUM_XZ; i++)
//        {
//            //for (int j = 0; j < WorldOld.CHUNK_NUM_Y; j++)
//            {
//                for (int k = 0; k < WorldOld.CHUNK_NUM_XZ; k++)
//                {
//                    Chunk chunk = currentWorld.getChunkAtIndex(i,0,k);
//                    if(chunk != null && !chunk.isEmpty())
//                    {
//                        chunk.setFlagForReMesh(false);
//                        System.out.println("Mesh created for Chunk located at: " + chunk.getPosition());
//                        chunks.add(new ChunkMesh(chunk, terrainTexture));
//                    }
//
//                }
//            }
//        }
//        //end temp
//
//        currentWorld.getEntities().add(new EntityHumanoid(0,0,0,0,0,0));



        //temp
        //onionEntMesh = new EntityMesh(onion, onionMesh, onionTexture);


//        for (Entity ent : WorldOld.getWorld().getEntities())
//        {
//            entityMeshes.add(new EntityMesh(ent, defaultMesh, entTexture));
//        }

        Random rand = new Random();
        //for (int i = 0; i < 16; i++)
        {
            structures.add(new EntityStructure(new StructureIsland(0, 0, 0)));
            structures.add(new EntityStructure(new StructureTree(new Vector3i(-10,-20, -8))));
        }

        Logger.info("Initialization finished in " + (System.currentTimeMillis() - initStartTime) + " miliseconds");
    }

    @Override
    public void input()
    {
        if(KeyboardInputHandler.isKeyDown(GLFW.GLFW_KEY_SPACE))
        {
            System.out.println("fizz");
        }
        KeyboardInputHandler.update();
        MouseInputHandler.update();
        MousePositionHandler.update();
    }

    private boolean moveSeaUp = false;
    private float seaheight = Tile.TILE_HEIGHT* WorldOld.SEA_LEVEL;;

    @Override
    public void update(float dt)
    {
        calendar.update();

        camera.move();

        skybox.setPosition(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
        skybox.rotate(0,0,15f*24f/(float)CalendarNorse.DAY_LENGTH);

        if(moveSeaUp)
        {
            seaheight+=0.0025;
            if(seaheight > Tile.TILE_HEIGHT* WorldOld.SEA_LEVEL+(Tile.TILE_HEIGHT/5)) moveSeaUp = false;
        }
        else
        {
            seaheight-=0.0025;
            if(seaheight < Tile.TILE_HEIGHT* WorldOld.SEA_LEVEL-(Tile.TILE_HEIGHT/5)) moveSeaUp = true;
        }
        sea.setPosition(camera.getPosition().x, seaheight, camera.getPosition().z);
        seaDeep.setPosition(camera.getPosition().x, 0, camera.getPosition().z);
        //sunLight.update();
        spotLight.setPosition(new Vector3f(camera.getPosition().x,camera.getPosition().y, camera.getPosition().z));

        //picker.update();
        //onion.setPosition(camera.getPosition().x, camera.getPosition().y-1, camera.getPosition().y);
        //System.out.println(picker.getCurrentRay());
        //System.out.println(picker.getCurrentWorldPoint());

        //currentWorld.update();

//        for (ChunkMesh mesh : chunks)
//        {
//            if(mesh.getChunk().isFlagForReMesh())
//            {
//                mesh.reloadMesh();
//                mesh.getChunk().setFlagForReMesh(false);
//            }
//        }

        for (EntityStructure structure : structures)
        {
            structure.update();
        }

        for (StructureMesh mesh : structuresMeshes)
        {
            if(mesh.getChunkColumn().isFlagForReMesh())
            {
                mesh.reloadMesh();
                mesh.getChunkColumn().wasReMeshed();
            }
        }

        if(MouseInputHandler.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT))
        {
            System.out.println("Time: " + (calendar.getTime()/(float)CalendarNorse.DAY_LENGTH) * 24f);
        }

        //Move to WorldOld.update()
//        for (Entity ent : WorldOld.getWorld().getEntities())
//        {
//            ent.update();
//        }

        if (KeyboardInputHandler.isKeyDown(GLFW.GLFW_KEY_F1))
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
//        if (KeyboardInputHandler.isKeyDown(GLFW.GLFW_KEY_1))
//        {
//            WorldOld.chunkTickSpeed = 4;
//        }
//        else if (KeyboardInputHandler.isKeyDown(GLFW.GLFW_KEY_2))
//        {
//            WorldOld.chunkTickSpeed = 8;
//        }
//        else if (KeyboardInputHandler.isKeyDown(GLFW.GLFW_KEY_3))
//        {
//            WorldOld.chunkTickSpeed = 16;
//        }
//        else if (KeyboardInputHandler.isKeyDown(GLFW.GLFW_KEY_4))
//        {
//            WorldOld.chunkTickSpeed = 32;
//        }
//        else if (KeyboardInputHandler.isKeyDown(GLFW.GLFW_KEY_5))
//        {
//            WorldOld.chunkTickSpeed = 64;
//        }
        /*
        else if (KeyboardInputHandler.isKeyDown(Keyboard.KEY_6))
        {
            WorldOld.chunkTickSpeed = 128;
        }
        else if (KeyboardInputHandler.isKeyDown(Keyboard.KEY_7))
        {
            WorldOld.chunkTickSpeed = 256;
        }
        else if (KeyboardInputHandler.isKeyDown(Keyboard.KEY_8))
        {
            WorldOld.chunkTickSpeed = 512;
        }*/
    }

    public static void addStructureMesh(ChunkColumn col)
    {
        structuresMeshes.add(new StructureMesh(col, terrainTexture));
    }

    public static void removeStructureMesh(ChunkColumn col)
    {
        for (StructureMesh mesh : structuresMeshes)
        {
            if(mesh.getChunkColumn() == col)
            {
                structuresMeshes.remove(mesh);
                loader.unloadMesh(mesh.getMesh());
            }
        }
    }


    @Override
    public void render()
    {
        //Display is cleared before drawing
        renderer.clear();
        rendererNoLight.clear();
        //start rendering
        //ent.translate(0.001f, 0.001f, 0);
        //ent.scale(-0.001f);
        //ent.rotate(0f, 0f,0.1f);

        shaderNoLight.start();

        shaderNoLight.loadViewMatrix(camera);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        rendererNoLight.render(skyEntMesh, shaderNoLight);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        //rendererNoLight.render(seaDeepEntMesh, shaderNoLight);

        shaderNoLight.stop();

        shader.start();
        shader.loadViewMatrix(camera);

        shader.loadAmbientLight(ambientLight);
        shader.loadDirectionalLight(sunLight);
//        shader.loadSpotLight(spotLight);




        //shader.loadTime(calendar.getTime()/CalendarNorse.DAY_LENGTH);

//        for (ChunkMesh chunk : chunks)
//        {
//            renderer.render(chunk, shader);
//        }
        for (StructureMesh mesh : structuresMeshes)
        {
            renderer.render(mesh, shader);
        }
//        for (EntityMesh mesh : entityMeshes)
//        {
//            renderer.render(mesh, shader);
//            //System.out.println("CameraController: " + CameraController.getChunkPosition());
//            //System.out.println(mesh.getEntity().getChunkPosition());
//        }

        //renderer.render(seaEntMesh, shader);

        //renderer.render(mesh, shader);
        shader.stop();
        //stop rendering
        //Displays to screen



        WindowManager.updateDisplay();
    }

    public void dispose()
    {
        shader.cleanUp();
        loader.cleanUp();
        WindowManager.dispose();
    }

    /**
     * The main method for the NorseTown game,
     * Loads the operating system dependent external libraries and launches the game
     *
     * @param args Not used
     */
    public static void main(String[] args)
    {
        String fileNatives = OperatingSystem.getOSforLWJGLNatives();
        System.setProperty("org.lwjgl.librarypath", (new File("libs" + File.separator + "native" + File.separator + fileNatives)).getAbsolutePath());
        System.out.println(System.getProperty("java.library.path"));
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


    private static float skyboxSize = 10;//(float)((Renderer.P_FAR_PLANE-10f)/(Math.sqrt(3)));
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

        try
        {
            // This line is critical for LWJGL's interoperation with GLFW's
            // OpenGL context, or any context that is managed externally.
            // LWJGL detects the context that is current in the current thread,
            // creates the GLCapabilities instance and makes the OpenGL
            // bindings available for use.
            GL.createCapabilities();

            // Set the clear color
            GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);


            game.init();

            while (isRunning && !GLFW.glfwWindowShouldClose(WindowManager.window))
            {
                firstTime = System.nanoTime() / 1_000_000_000.0;
                passedTime = firstTime - lastTime;
                lastTime = firstTime;

                unprocessedTime += passedTime;
                frameTime += passedTime;

                while (unprocessedTime >= UPDATE_CAP)
                {
                    unprocessedTime -= UPDATE_CAP;
                    render = true;

                    game.input();
                    game.update((float) UPDATE_CAP);


                    //FPS output
                    if (MODE.equals("debug"))
                    {
                        if (frameTime >= 1.0)
                        {
                            frameTime = 0;
                            fps = frames;
                            frames = 0;
                        }
                    }
                }

                if (render)
                {
                    game.render();

                    if (MODE.equals("debug") && outputFPS)
                    {
                        //renderer.drawText("FPS: " + fps, 0, 0, 0xff_00_ff_ff);
                        System.out.println("FPS: " + fps);
                    }

                    WindowManager.resize();
                    frames++;
                } else
                {
                    try
                    {
                        Thread.sleep(1);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e)
        {
            //TODO write to a file
            e.printStackTrace();
        }
        dispose();
    }
}

