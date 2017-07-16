package com.deadvikingstudios.norsetown.controller;


import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.humanoids.EntityHumanoid;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.Chunk;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.view.lwjgl.DisplayManager;
import com.deadvikingstudios.norsetown.view.lwjgl.Loader;
import com.deadvikingstudios.norsetown.view.lwjgl.renderers.MasterRenderer;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import com.deadvikingstudios.norsetown.view.meshes.ChunkMesh;
import com.deadvikingstudios.norsetown.view.meshes.EntityMesh;
import com.deadvikingstudios.norsetown.view.meshes.MeshTexture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

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
    public static final String VERSION = "Indev-0.01d";

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

    private static MeshTexture grassTexture;
    private static MeshTexture entTexture;

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
                InputKeyboard.update();

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

    //TODO: make better and move to treeGen
    private void makeTree(int x, int height, int z, int leafstart)
    {
        for (int j = Chunk.CHUNK_HEIGHT - 1; j >= 0; --j)
        {
            if(World.getWorld().getTileAt(x,j,z) != 0)
            {
                System.out.println("Tree created at: " + x + "," + j + "," + z);
                for (int i = 1; i < height+1; i++)
                {
                    World.getWorld().setTile(Tile.Tiles.tileLog, x, j+i, z);
                    //world.setTile(Tile.Tiles.tileLog, x, j+i, z);
                    if(i-leafstart > 0)
                    {
                        World.getWorld().setTile(Tile.Tiles.tileLeaves, x+1, j+i, z);
                        World.getWorld().setTile(Tile.Tiles.tileLeaves, x-1, j+i, z);
                        World.getWorld().setTile(Tile.Tiles.tileLeaves, x, j+i, z+1);
                        World.getWorld().setTile(Tile.Tiles.tileLeaves, x, j+i, z-1);
                    }
                    World.getWorld().setTile(Tile.Tiles.tileLeaves, x, j+i+1, z);
                }
                return;
            }
        }
    }

    public void init()
    {
        Tile.Tiles.init();


        CameraController.setRotation(35, 135, 0);
        CameraController.setPosition(0, Chunk.CHUNK_HEIGHT * 0.5f * Tile.TILE_HEIGHT, 0);

        grassTexture = new MeshTexture(loader.loadTexture("textures/terrain"));//"textures/tiles/grass_top"));
        entTexture = new MeshTexture(loader.loadTexture("textures/entTexture"));
        currentWorld = new World(1);

        Random random = World.getWorld().getRandom();

        //this isnt working because it's not updating the mesh!!!
        for (int i = 0; i < random.nextInt(World.CHUNK_NUM_XZ * 2) + World.CHUNK_NUM_XZ; i++)
        {
            makeTree(random.nextInt(World.CHUNK_NUM_XZ * Chunk.CHUNK_SIZE), random.nextInt(Chunk.CHUNK_HEIGHT / 4) + Chunk.CHUNK_HEIGHT/4, random.nextInt(World.CHUNK_NUM_XZ * Chunk.CHUNK_SIZE), 6);
        }

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



        World.getWorld().getEntities().add(new EntityHumanoid(Chunk.CHUNK_SIZE, 10, Chunk.CHUNK_SIZE, 0, 0, 0));

        float[] vertices = {
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

        float[] uv = {
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

        int[] indices = {
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

        for (Entity ent : World.getWorld().getEntities())
        {
            entityMeshes.add(new EntityMesh(ent, loader.loadToVAO(vertices, indices, uv), entTexture));
        }
    }

    private boolean renderWireFrame = false;

    public void update(float dt)
    {
        if (InputKeyboard.getKeyDown(Keyboard.KEY_F1))
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


        CameraController.move();
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
        for (EntityMesh mesh : entityMeshes)
        {
            renderer.render(mesh, shader);
            //System.out.println("CameraController: " + CameraController.getPosition());
            //System.out.println(mesh.getEntity().getPosition());
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
        System.setProperty("org.lwjgl.librarypath", new File("libs" + File.separator + "native" + File.separator + fileNatives).getAbsolutePath());
        new GameContainer().start();
    }
}
