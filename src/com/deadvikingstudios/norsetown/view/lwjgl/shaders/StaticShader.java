package com.deadvikingstudios.norsetown.view.lwjgl.shaders;

import com.deadvikingstudios.norsetown.controller.MainGameLoop;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class StaticShader extends ShaderProgram
{
    private static final String VERTEX_FILE = MainGameLoop.SRC_PATH + "view/lwjgl/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = MainGameLoop.SRC_PATH + "view/lwjgl/shaders/fragmentShader.txt";

    public StaticShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uvs");
    }
}
