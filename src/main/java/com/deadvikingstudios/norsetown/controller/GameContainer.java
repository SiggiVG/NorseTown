package com.deadvikingstudios.norsetown.controller;


import com.deadvikingstudios.norsetown.controller.input.KeyboardInputHandler;
import com.deadvikingstudios.norsetown.controller.input.MouseInputHandler;
import com.deadvikingstudios.norsetown.controller.input.MousePositionHandler;
import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntitySkybox;
import com.deadvikingstudios.norsetown.model.entities.ai.Task;
import com.deadvikingstudios.norsetown.model.items.Item;
import com.deadvikingstudios.norsetown.model.lighting.DirectionalLight;
import com.deadvikingstudios.norsetown.model.lighting.SpotLight;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.CalendarNorse;
import com.deadvikingstudios.norsetown.model.world.WaterTile;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.model.world.structures.ChunkColumn;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import com.deadvikingstudios.norsetown.view.Loader;
import com.deadvikingstudios.norsetown.view.WaterFrameBuffers;
import com.deadvikingstudios.norsetown.view.WindowManager;
import com.deadvikingstudios.norsetown.view.guis.GuiRenderer;
import com.deadvikingstudios.norsetown.view.guis.GuiTexture;
import com.deadvikingstudios.norsetown.view.meshes.*;
import com.deadvikingstudios.norsetown.view.renderers.Renderer;
import com.deadvikingstudios.norsetown.view.renderers.LightlessRenderer;
import com.deadvikingstudios.norsetown.view.renderers.RendererWater;
import com.deadvikingstudios.norsetown.view.shaders.LightlessStaticShader;
import com.deadvikingstudios.norsetown.view.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.shaders.WaterShader;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 *
 */
public class GameContainer implements Runnable, IGameContainer
{
    public static final String GAME_NAME = "NorseTown";
    public static final String VERSION = "Indev-0.04";

    public static final String DEBUG = "debug";
    public static String MODE = DEBUG;
    private boolean outputFPS = false;

//    public static final String SRC_PATH = "/src/main/java/com/deadvikingstudios/norsetown/";
//    private static boolean outputFPS = false;

    private Thread thread;
    /**
     * this, the current instance.
     */
    public static GameContainer game;

    private boolean isRunning = false;
    private static final int TARGET_FPS = 60;

    public static Loader loader = null;
    //TODO: setup HashMap<String, Shader>


    public static final float SEA_LEVEL = -5f;

    public static RendererWater waterRenderer = null;

//    public static WaterShader waterShader = null;
//    public static WaterRenderer waterRenderer = null;

    //TODO: setup HashMap<String, Renderer>
    public static Renderer renderer = null;
    private static LightlessRenderer lightlessRenderer = null;
    private static GuiRenderer guiRenderer = null;

//    private static List<StructureMesh> structuresMeshes = new ArrayList<StructureMesh>();
    private static List<ChunkColMesh> structuresMeshes = new ArrayList<ChunkColMesh>();
    private static List<ChunkColMesh> structuresMeshesToRemove = new ArrayList<ChunkColMesh>();
    private static List<EntityMesh> entityMeshes = new ArrayList<EntityMesh>();
    private static List<EntityMesh> entityMeshesToRemove = new ArrayList<EntityMesh>();

    private static RawMesh defaultMesh;

    /**
     * The Camera
     */
    private static CameraController camera;

    private static boolean renderWireFrame = false;

    //**** GUI ****//
    List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();

    //**** SKYBOX ****//
    private static Entity skybox;
    public static SkyboxMesh skyEntMesh;
    private static MeshTexture skyboxTexture;

    //**** GLOBAL LIGHTING ****//
    public static Vector3f ambientLight = new Vector3f(0.4f,0.4f,0.4f);
    public static DirectionalLight sunLight;
    private static SpotLight spotLight;
    //private static SunLight curSunLight;

    //***** SEA ****//
    List<WaterTile> waters  = new ArrayList<WaterTile>();
    private static WaterFrameBuffers waterFBOs;

    //**** TERRAIN TEXTURE ****//
    private static MeshTexture terrainTexture;

    //TEST
    private static MeshTexture skullTexture;

    /**
     * The current world object
     */
    private static World currentWorld;

    public GameContainer() {this.game = this;}


    private void start()
    {
        WindowManager.create();

        Logger.debug("Loading Shaders");
        double startTime = System.currentTimeMillis();

        loader = new Loader();

        //**** GUI ****//
        guiRenderer = new GuiRenderer(loader);

        //**** Default(Lit) Shader Set-Up ****//
        renderer = new Renderer(new StaticShader());

        //**** Lightless Shader Set-Up ****//
        lightlessRenderer = new LightlessRenderer(new LightlessStaticShader());

        //**** Water Renderer Set-Up ****//
        waterFBOs = new WaterFrameBuffers();
        waterRenderer  = new RendererWater(loader, new WaterShader(), renderer.getProjectionMatrix(), waterFBOs);
//        guiTextures.add(new GuiTexture(waterFBOs.getRefractionTexture(), new Vector2f(0.75f,0.75f), new Vector2f(0.25f,0.25f)));
//        guiTextures.add(new GuiTexture(waterFBOs.getReflectionTexture(), new Vector2f(0.25f,0.75f), new Vector2f(0.25f,0.25f)));

        Logger.debug("Shader Loading finished in " + (System.currentTimeMillis() - startTime) + " miliseconds");

        thread = new Thread(this);
        thread.run();
    }

    private void initTextures()
    {
        //**** SKYBOX ****//
        skyboxTexture = new MeshTexture(loader.loadTexture("textures/skybox"));
        //**** TERRAIN ****//
        terrainTexture = new MeshTexture(loader.loadTexture("textures/terrain"));//"textures/tiles/grass_top"));
        //**** TEST ****//
        skullTexture = new MeshTexture(loader.loadTexture("textures/skull_logo"));
    }

    private void initGlobalLights()
    {
        sunLight = new DirectionalLight(new Vector3f(1f,1f,1f), new Vector3f(0.6f,-1f,0.2f), 1.0f);
        spotLight = new SpotLight(new Vector3f(0.8f,0.8f,1f), new Vector3f(0,0,0), 1.0f);
    }

    private void initCamera()
    {
        camera = new CameraController(0,0,0);//-30, 20, -30);
//        camera.setRotation(35, 135, 0);//135
    }

    public void stop()
    {
        this.isRunning = false;
    }

    @Override
    public void init()
    {
        Logger.info("Initialization started");
        double initStartTime = System.currentTimeMillis();

        initTextures();
        initGlobalLights();
        initCamera();

        Item.Items.init();
        Tile.Tiles.init();

//        TextureAtlas.instance.CreateAtlas();

        defaultMesh = loader.loadToVAO(CubeMesh.getVertices(1,1,true), CubeMesh.indices, CubeMesh.uv, CubeMesh.cubeNormals);

        //x=-65 is about the latitude of iceland, z=-90 is darbreak at the equator
        skybox = new EntitySkybox(0,0,0,-65,0,-90);
        RawMesh skyMesh = loader.loadToVAO(SkyboxMesh.skyVertices, CubeMesh.indices, SkyboxMesh.skyboxUV, SkyboxMesh.skyBoxNormals);
        skyEntMesh = new SkyboxMesh(skybox, skyMesh, skyboxTexture);

        //SEA
        waters.add(new WaterTile(0,0,SEA_LEVEL));

        //TERRAIN TEXTURE MUST BE INITIALIZED BEFORE A WORLD IS CREATED
        currentWorld = new World(System.currentTimeMillis());

        Logger.info("Initialization finished in " + (System.currentTimeMillis() - initStartTime) + " miliseconds");
    }

    @Override
    public void input()
    {
//        KeyboardInputHandler.update();
        MouseInputHandler.update();
        MousePositionHandler.update();
    }

    @Override
    public void update(float dt)
    {
        camera.move();

        skybox.rotate(0,0,15f*24f/(float)CalendarNorse.DAY_LENGTH);

        //sunLight.update();
        spotLight.setPosition(new Vector3f(camera.getPosition().x,camera.getPosition().y, camera.getPosition().z));

        currentWorld.update();

        if(KeyboardInputHandler.isKeyPressed(GLFW.GLFW_KEY_SPACE))
        {
            //currentWorld.cutDownTrees();

        }

        //Chunk Structures
        for (ChunkColMesh mesh : structuresMeshesToRemove)
        {
            structuresMeshes.remove(mesh);
            loader.unloadMesh(mesh.getMesh());
        }
        structuresMeshesToRemove.clear();

        for (ChunkColMesh mesh : structuresMeshes)
        {
            if(mesh.getChunkColumn() == null)
            {
                structuresMeshesToRemove.add(mesh);
            }
            else if(mesh.getChunkColumn().isFlagForReMesh())
            {
                mesh.reloadMesh();
                mesh.getChunkColumn().wasReMeshed();
            }
        }

        //Entities
        entityMeshes.removeAll(entityMeshesToRemove);
        entityMeshesToRemove.clear();

        for(EntityMesh mesh : entityMeshes)
        {
            if(mesh.getEntity() == null)
            {
                entityMeshesToRemove.add(mesh);
            }
        }

        if (KeyboardInputHandler.isKeyPressed(GLFW.GLFW_KEY_F1))
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
        structuresMeshes.add(new ChunkColMesh(col, terrainTexture));
    }

    public static void removeStructureMesh(ChunkColumn col)
    {
        for (ChunkColMesh mesh : structuresMeshes)
        {
            if(mesh.getChunkColumn() == col)
            {
                structuresMeshesToRemove.add(mesh);
            }
        }
    }

    public static void addEntity(Entity entity)
    {
        entityMeshes.add(new EntityMesh(entity, defaultMesh, skullTexture));
    }

    public static void removeEntity(Entity entity)
    {
        for (EntityMesh mesh : entityMeshes)
        {
            if(mesh.getEntity() == entity)
            {
                entityMeshesToRemove.add(mesh);
            }
        }
    }


    @Override
    public void render()
    {
        //Display is cleared before drawing
        renderer.clear();

        //**** START RENDERING ****//

        //shader.loadSpotLight(spotLight);

        GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

        //**** START FBO RENDERING ****//
        //Reflection
        waterFBOs.bindReflectionFrameBuffer();
        renderer.clear();
        float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
        camera.getPosition().y -= distance;
        //if using roll (z rotation) I need to invert that too
        camera.invertPitch();
        lightlessRenderer.renderScene(entityMeshes,structuresMeshes,camera);
        renderer.renderScene(entityMeshes, structuresMeshes, camera, new Vector4f(0,1,0,-waters.get(0).getHeight()+0.1f));
        camera.invertPitch();
        camera.getPosition().y += distance;
//        waterFBOs.unbindCurrentFrameBuffer();
        //Refraction
        waterFBOs.bindRefractionFrameBuffer();
        renderer.clear();
        GL11.glDisable(GL11.GL_CULL_FACE);
//        lightlessRenderer.renderScene(null,structuresMeshes,camera);
        renderer.renderScene(entityMeshes, structuresMeshes, camera, new Vector4f(0,-1,0,waters.get(0).getHeight()-1));
        GL11.glEnable(GL11.GL_CULL_FACE);
        waterFBOs.unbindCurrentFrameBuffer();
        //**** STOP FBO RENDERING ****//

        //Some drivers have issues with this command not actually disabling it. Mine works fine, however.
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
        lightlessRenderer.renderScene(entityMeshes,structuresMeshes,camera);
        renderer.renderScene(entityMeshes, structuresMeshes, camera, new Vector4f(0,1,0,-waters.get(0).getHeight()));
        waterRenderer.render(waters, camera);

        //GUI
        guiRenderer.render(guiTextures);

        //**** STOP RENDERING ****//

        //Displays to screen
        WindowManager.updateDisplay();
    }

    private void dispose()
    {
        waterFBOs.cleanUp();

        guiRenderer.cleanUp();
        renderer.cleanUp();
        lightlessRenderer.cleanUp();
        waterRenderer.cleanUp();

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

//        double firstTime;
//        firstTime = System.nanoTime();
//        for (int i = 0; i < 1_000_000_000; i++)
//        {
//            int n = (int) (100 * 0.2);
//        }
//        System.out.println((System.nanoTime() - firstTime));
//
//        firstTime = System.nanoTime();
//        for (int i = 0; i < 1_000_000_000; i++)
//        {
//            int n = 100 / 5;
//        }
//        System.out.println((System.nanoTime() - firstTime));


        new GameContainer().start();
    }

    public static RawMesh getDefaultMesh()
    {
        return defaultMesh;
    }

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

                double updateCap = 1.0 / (double) TARGET_FPS;

                while (unprocessedTime >= updateCap)
                {
                    unprocessedTime -= updateCap;
                    render = true;

                    game.input();
                    game.update((float) updateCap);


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

    public static Vector3i getCameraPosition()
    {
        Vector3f pos = camera.getPosition();
        return new Vector3i(Math.round(pos.x), 0, Math.round(pos.z));
    }
}