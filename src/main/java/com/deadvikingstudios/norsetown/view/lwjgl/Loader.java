package com.deadvikingstudios.norsetown.view.lwjgl;

import com.deadvikingstudios.norsetown.view.meshes.RawMesh;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiggiVG on 6/19/2017.
 *
 * Holds pointers to VAOs and VBOs
 * as well as loads/interfaces with them for transmittance of data
 */
public class Loader
{
    private static List<Integer> vaos = new ArrayList<Integer>();
    private static List<Integer> vbos = new ArrayList<Integer>();
    private static List<Integer> textures = new ArrayList<Integer>();

    /**
     *
     * @param vertices an array of coordinates
     * @return the model for rendering
     */
    public RawMesh loadToVAO(float[] vertices, int[] indices, float[] uvs)
    {
        int vaoID = createVAO();
        storeDataInAttributeList(vertices, 0, 3);
        bindIndicesBuffer(indices);
        storeDataInAttributeList(uvs, 1, 2);
        GL30.glBindVertexArray(0);

        return new RawMesh(vaoID, indices.length);
    }

    private int createVAO()
    {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    private void storeDataInAttributeList(float[] data, int attributeNumber, int dimensions)
    {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, dimensions, GL11.GL_FLOAT, false, 0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices)
    {
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * For Vertices
     * @param data
     * @return
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    /**
     * For Indices/Triangles
     * @param data
     * @return
     */
    private IntBuffer storeDataInIntBuffer(int[] data)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    /**
     * Images must be square and have a height/width that is a power of 2
     *
     * This adds the "/res/" and the ".png" for you
     *
     * @param filePath
     * @return
     */
    public int loadTexture(String filePath)
    {
        Texture texture = null;
        try
        {
            texture = TextureLoader.getTexture("PNG", this.getClass().getResourceAsStream("/" +  filePath + ".png"), GL11.GL_NEAREST);
        }catch (FileNotFoundException e)
        {
            System.err.println("Texture not found: " + filePath);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    /**
     * Empties the data from the VertexArrayObject and VertexBufferObject
     */
    public void cleanUp()
    {
        for (int vao : vaos)
        {
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo : vbos)
        {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textures)
        {
            GL11.glDeleteTextures(texture);
        }
    }
}
