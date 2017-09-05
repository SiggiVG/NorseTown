package com.deadvikingstudios.norsetown.model.world;

/**
 * Created by SiggiVG on 6/21/2017.
 */
public class EmptyChunk extends Chunk
{

    /**
     * Default Constructor
     *
     * @param x x position of the west face
     * @param y y position of the bottom face
     * @param z z position of the south face
     */
    public EmptyChunk(float x, float y, float z)
    {
        super(x, y, z);
    }

    @Override
    protected void init(){}
}
