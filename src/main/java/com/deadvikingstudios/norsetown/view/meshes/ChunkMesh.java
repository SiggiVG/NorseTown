package com.deadvikingstudios.norsetown.view.meshes;

import com.deadvikingstudios.norsetown.model.ArrayUtils;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.Chunk;
import com.deadvikingstudios.norsetown.model.world.World;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static com.deadvikingstudios.norsetown.controller.GameContainer.loader;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * This is how Chunks Render, and gets updated whenever a chunk does
 */
public class ChunkMesh extends TexturedMesh
{
    private Chunk chunk;

    public ChunkMesh(Chunk chunk, MeshTexture texture)
    {
        this.chunk = chunk;
        this.texture = texture;

        List<Float> vertices = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();
        List<Float> uvs = new ArrayList<Float>();

        CreateMesh(vertices, indices, uvs);

        rawMesh = loader.loadToVAO(ArrayUtils.floatFromFloat(vertices),
                ArrayUtils.intFromInteger(indices), ArrayUtils.floatFromFloat(uvs));
    }

    public Vector3f getPosition()
    {
        return chunk.getPosition();
    }

    private void CreateMesh(List<Float> vertices, List<Integer> indices, List<Float> uvs)
    {
        for (int i = 0; i < Chunk.CHUNK_SIZE; i++)
        {
            for (int j = 0; j < Chunk.CHUNK_HEIGHT; j++)
            {
                for (int k = 0; k < Chunk.CHUNK_SIZE; k++)
                {
                    if(chunk.getTileAt(i,j,k) == 0)
                    {
                        continue;
                    }

                    CreateCube(vertices, indices, uvs, i,j,k);
                    //System.out.println(vertices);
                    //System.out.println(indices);
                }
            }

        }
    }

    private void CreateCube(List<Float> vertices, List<Integer> indices, List<Float> uvs, int x, int y, int z)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + World.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ);

        //TODO
        //North
        if(chunk.getTileAt(x,y,z+1) == 0)
        {
            verts = getFaceVertices(0, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            for (int i = 0; i < uvListXZ.length; i++)
            {
                uvs.add(uvListXZ[i]);
            }
        }
        //east
        if(chunk.getTileAt(x+1,y,z) == 0)
        {
            verts = getFaceVertices(1, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            for (int i = 0; i < uvListXZ.length; i++)
            {
                uvs.add(uvListXZ[i]);
            }
        }
        //south
        if(chunk.getTileAt(x,y,z-1) == 0)
        {
            verts = getFaceVertices(2, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            for (int i = 0; i < uvListXZ.length; i++)
            {
                uvs.add(uvListXZ[i]);
            }
        }
        //west
        if(chunk.getTileAt(x-1,y,z) == 0)
        {
            verts = getFaceVertices(3, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            for (int i = 0; i < uvListXZ.length; i++)
            {
                uvs.add(uvListXZ[i]);
            }
        }
        //top
        if(chunk.getTileAt(x,y+1,z) == 0)
        {
            verts = getFaceVertices(4, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            for (int i = 0; i < uvListY.length; i++)
            {
                uvs.add(uvListY[i]);
            }
        }
        //bottom
        if(chunk.getTileAt(x,y-1,z) == 0)
        {
            verts = getFaceVertices(5, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            for (int i = 0; i < uvListY.length; i++)
            {
                uvs.add(uvListY[i]);
            }
        }

    }

    private static float[][] vertexList =
    {
            new float[]{ 0,             0,               0}, //0
            new float[]{ Tile.TILE_SIZE,0,               0}, //1
            new float[]{ 0,             0,               Tile.TILE_SIZE}, //2
            new float[]{ Tile.TILE_SIZE,0,               Tile.TILE_SIZE}, //3

            new float[]{ 0,             Tile.TILE_HEIGHT,0}, //4
            new float[]{ Tile.TILE_SIZE,Tile.TILE_HEIGHT,0}, //5
            new float[]{ 0,             Tile.TILE_HEIGHT,Tile.TILE_SIZE}, //6
            new float[]{ Tile.TILE_SIZE,Tile.TILE_HEIGHT,Tile.TILE_SIZE}  //7

            /*
            new float[]{-0.5f,-0.5f,-0.5f}, //0
            new float[]{0.5f,-0.5f,-0.5f}, //1
            new float[]{-0.5f,-0.5f,0.5f}, //2
            new float[]{0.5f,-0.5f,0.5f}, //3

            new float[]{-0.5f,0.5f,-0.5f}, //4
            new float[]{0.5f,0.5f,-0.5f}, //5
            new float[]{-0.5f,0.5f,0.5f}, //6
            new float[]{0.5f,0.5f,0.5f}  //7
            */
    };

    private static int[] indexList =
    {
            0,1,3,
            3,1,2
    };

    private static float[] uvListY =
    {
            0,0,
            0,1,
            1,1,
            1,0
    };

    private static float[] uvListXZ =
            {
                    0,0,
                    0,Tile.TILE_HEIGHT,
                    1,Tile.TILE_HEIGHT,
                    1,0
            };

    private static int[][] faceVertices =
    {
            new int[] { 6,2,3,7 },//north
            new int[] { 5,1,3,7 },//east
            new int[] { 4,0,1,5 },//south
            new int[] { 4,0,2,6 },//west
            new int[] { 6,4,5,7 },//top
            new int[] { 2,0,1,3 },//bottom
    };

    /**
     * @param dir the face being drawn, 0,1,2,3,4,5 = North, East, South, West, Top, Bottom
     * @param vec
     * @return
     */
    private static float[] getFaceVertices(int dir, Vector3f vec)
    {
        float[] fv = new float[4*3];
        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexList[faceVertices[dir][i]][0] + vec.x;
            fv[i*3+1] = vertexList[faceVertices[dir][i]][1] + vec.y;
            fv[i*3+2] = vertexList[faceVertices[dir][i]][2] + vec.z;
        }
        return fv;
    }
    /*private static float[] getFaceVertices(int dir, float x, float y, float z)
    {
        float[] fv = new float[4*3];
        for (int i = 0; i < fv.length; i+=3)
        {
            fv[i] = vertexList[dir][i] + x;
            fv[i+1] = vertexList[dir][i+1] + y;
            fv[i+2] = vertexList[dir][i+2] + z;
        }
        return fv;
    }*/

}
