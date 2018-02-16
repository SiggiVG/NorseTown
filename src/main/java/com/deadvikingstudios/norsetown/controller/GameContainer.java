package com.deadvikingstudios.norsetown.controller;


import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityStructure;
import com.deadvikingstudios.norsetown.model.lighting.DirectionalLight;
import com.deadvikingstudios.norsetown.model.lighting.SpotLight;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.CalendarNorse;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.model.world.WorldOld;
import com.deadvikingstudios.norsetown.model.world.structures.ChunkColumn;
import com.deadvikingstudios.norsetown.model.world.structures.StructureIsland;
import com.deadvikingstudios.norsetown.model.world.structures.StructureTree;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector2i;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import com.deadvikingstudios.norsetown.view.TextureAtlas;
import com.deadvikingstudios.norsetown.view.lwjgl.Loader;
import com.deadvikingstudios.norsetown.view.lwjgl.WaterFrameBuffers;
import com.deadvikingstudios.norsetown.view.lwjgl.WindowManager;
import com.deadvikingstudios.norsetown.view.lwjgl.renderers.Renderer;
import com.deadvikingstudios.norsetown.view.lwjgl.renderers.RendererNoLight;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.LightlessStaticShader;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static final String DEBUG = "debug";
    public static String MODE = DEBUG;
    private boolean outputFPS = false;

//    public static final String SRC_PATH = "/src/main/java/com/deadvikingstudios/norsetown/";
//    private static boolean outputFPS = false;

    private Thread thread;
    private GameContainer game;

    private boolean isRunning = false;
    private static final int TARGET_FPS = 60;
    private final double UPDATE_CAP = 1.0/(double)TARGET_FPS;

    public static Loader loader = null;
    //TODO: setup HashMap<String, Shader>
    public static StaticShader shader = null;
    public static StaticShader shaderNoLight = null;

//    public static WaterShader waterShader = null;
//    public static WaterRenderer waterRenderer = null;

    //TODO: setup HashMap<String, Renderer>
    public static Renderer renderer = null;
    private static RendererNoLight rendererNoLight = null;

    private static List<StructureMesh> structuresMeshes = new ArrayList<StructureMesh>();
    private static List<EntityMesh> entityMeshes = new ArrayList<EntityMesh>();

    private static RawMesh defaultMesh;

    private static CameraController camera;
    //private static MousePicker picker;

    private static boolean renderWireFrame = false;

    //**** SKYBOX ****
    private static Entity skybox;
    private static RawMesh skyMesh;
    public static EntityMesh skyEntMesh;
    private static MeshTexture skyboxTexture;

    //**** GLOBAL LIGHTING ****//
    public static Vector3f ambientLight = new Vector3f(0.4f,0.4f,0.4f);
    public static DirectionalLight sunLight;
    private static SpotLight spotLight;
    //private static SunLight curSunLight;

    //***** SEA ****//
//    List<WaterTile> waters  = new ArrayList<WaterTile>();
    private static WaterFrameBuffers fbos;

    //**** TERRAIN TEXTURE ****//
    private static MeshTexture terrainTexture;

    /**
     * The current world object
     */
    private static World currentWorld;

    public GameContainer() {this.game = this;}

    public void start()
    {
        WindowManager.create();

        loader = new Loader();

        //**** Default(Lit) Shader Set-Up ****//
        shader = new StaticShader();
        renderer = new Renderer(shader);

        //**** Lightless Shader Set-Up ****//
        shaderNoLight = new LightlessStaticShader();
        rendererNoLight = new RendererNoLight(shaderNoLight);

        //**** Water Renderer Set-Up ****//
//        waterShader = new WaterShader();
//        waterRenderer  = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
        fbos = new WaterFrameBuffers();

        thread = new Thread(this);
        thread.run();
    }

    private void initTextures()
    {
        //**** SKYBOX ****//
        skyboxTexture = new MeshTexture(loader.loadTexture("textures/skybox"));
        //**** TERRAIN ****//
        terrainTexture = new MeshTexture(loader.loadTexture("textures/terrain"));//"textures/tiles/grass_top"));
    }

    private void initGlobalLights()
    {
        sunLight = new DirectionalLight(new Vector3f(1f,1f,1f), new Vector3f(0.6f,-1f,0.2f), 1.0f);
        spotLight = new SpotLight(new Vector3f(0.8f,0.8f,1f), new Vector3f(0,0,0), 1.0f);
    }

    private void initCamera()
    {
        camera = new CameraController(0, 0, 0);
        //camera.setRotation(35, 135, 0);//135
    }

    public void stop()
    {
        this.isRunning = false;
    }

    private double initStartTime;

    @Override
    public void init()
    {
        Logger.info("Initialization started");
        initStartTime = System.currentTimeMillis();

        initTextures();
        initGlobalLights();
        initCamera();

        Tile.Tiles.init();

//        TextureAtlas.instance.CreateAtlas();

        defaultMesh = loader.loadToVAO(vertices, indices, uv, cubeNormals);

        skybox = new Entity(0,0,0,0,0,180);
        skyMesh = loader.loadToVAO(skyVertices, indices, skyboxUV, skyBoxNormals);
        skyEntMesh = new EntityMesh(skybox, skyMesh, skyboxTexture);

        //SEA
//        waters.add(new WaterTile(0,0,0));

        //TERRAIN TEXTURE MUST BE INITIALIZED BEFORE A WORLD IS CREATED
        currentWorld = new World(System.currentTimeMillis());

        Logger.info("Initialization finished in " + (System.currentTimeMillis() - initStartTime) + " miliseconds");
    }

    @Override
    public void input()
    {
        KeyboardInputHandler.update();
        MouseInputHandler.update();
        MousePositionHandler.update();
    }

    private boolean moveSeaUp = false;

    @Override
    public void update(float dt)
    {
        camera.move();

        skybox.setPosition(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
        skybox.rotate(0,0,15f*24f/(float)CalendarNorse.DAY_LENGTH);

        //sunLight.update();
        spotLight.setPosition(new Vector3f(camera.getPosition().x,camera.getPosition().y, camera.getPosition().z));

        currentWorld.update();

        for (StructureMesh mesh : structuresMeshes)
        {
            if(mesh.getChunkColumn().isFlagForReMesh())
            {
                mesh.reloadMesh();
                mesh.getChunkColumn().wasReMeshed();
            }
        }

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

        //**** START RENDERING ****//

        shader.loadSpotLight(spotLight);

        //**** START FBO RENDERING ****//
        fbos.bindReflectionFrameBuffer();
        renderer.renderScene(null, structuresMeshes, camera);
        fbos.unbindCurrentFrameBuffer();

        //**** STOP FBO RENDERING ****//

        rendererNoLight.renderScene(null,structuresMeshes,camera);
        renderer.renderScene(null, structuresMeshes, camera);

        //**** STOP RENDERING ****//

        //Displays to screen
        WindowManager.updateDisplay();
    }

    public void dispose()
    {
        fbos.cleanUp();
        shader.cleanUp();
        shaderNoLight.cleanUp();
//        waterShader.cleanUp();

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
        //Logger.debug(System.getProperty("java.library.path"));
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

                    if (MODE.equals(DEBUG) && outputFPS)
                    {
//                        renderer.drawText("FPS: " + fps, 0, 0, 0xff_00_ff_ff);
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

