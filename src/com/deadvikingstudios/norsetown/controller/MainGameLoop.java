package com.deadvikingstudios.norsetown.controller;

import com.deadvikingstudios.norsetown.view.lwjgl.DisplayManager;
import com.deadvikingstudios.norsetown.view.lwjgl.Loader;
import com.deadvikingstudios.norsetown.view.lwjgl.MasterRenderer;
import com.deadvikingstudios.norsetown.view.lwjgl.models.ModelTexture;
import com.deadvikingstudios.norsetown.view.lwjgl.models.RawModel;
import com.deadvikingstudios.norsetown.view.lwjgl.models.TexturedModel;
import com.deadvikingstudios.norsetown.view.lwjgl.shaders.StaticShader;
import org.lwjgl.opengl.Display;

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

    public static void main(String[] args)
    {
        DisplayManager.createDisplay();
        loader = new Loader();
        MasterRenderer renderer = new MasterRenderer();
        StaticShader shader = new StaticShader();


        float[] vertices =
                {
                        -0.5f,  0.5f, 0, //V0
                        -0.5f, -0.5f, 0, //V1
                         0.5f, -0.5f, 0, //V2
                         0.5f,  0.5f, 0, //V3
                };
        int[] indices =
                {
                        0, 1, 3, //Top Left Triangle
                        3, 1, 2 //Bottom Right Triangle
                };
        float[] uvs =
                {
                        0,0,
                        0,1,
                        1,1,
                        1,0,
                };

        RawModel model = loader.loadToVAO(vertices, indices, uvs);
        ModelTexture texture = new ModelTexture(loader.loadTexture("textures/skull_logo"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        while(!Display.isCloseRequested())
        {
            //Game Logic
            //Display is cleared before drawing
            renderer.prepare();
            //start rendering
            shader.start();
            renderer.render(texturedModel);
            shader.stop();
            //stop rendering
            //Displays to screen
            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
