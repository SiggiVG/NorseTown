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
    protected Chunk chunk;

    public ChunkMesh(Chunk chunk, MeshTexture texture)
    {
        this.chunk = chunk;
        this.texture = texture;

        List<Float> vertices = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();
        List<Float> uvs = new ArrayList<Float>();

        createMesh(vertices, indices, uvs);

        rawMesh = loader.loadToVAO(ArrayUtils.floatFromFloat(vertices),
                ArrayUtils.intFromInteger(indices), ArrayUtils.floatFromFloat(uvs));
    }

    protected ChunkMesh(){}

    public Vector3f getPosition()
    {
        return chunk.getPosition();
    }

    public Chunk getChunk()
    {
        return chunk;
    }

    protected void createMesh(List<Float> vertices, List<Integer> indices, List<Float> uvs)
    {
        for (int i = 0; i < Chunk.CHUNK_SIZE; i++)
        {
            for (int j = 0; j < Chunk.CHUNK_HEIGHT; j++)
            {
                for (int k = 0; k < Chunk.CHUNK_SIZE; k++)
                {
                    if (chunk.getTileAt(i, j, k) == 0)
                    {
                        continue;
                    }

                    /*if(!Tile.Tiles.get(chunk.getTileAt(i,j,k)).isOpaque())
                    {
                        //TODO Transparency Sorting
                        continue;
                    }*/
                    CreateCube(vertices, indices, uvs, i,j,k);
                }
            }

        }
    }

    private void CreateCube(List<Float> vertices, List<Integer> indices, List<Float> uvs, int x, int y, int z)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + World.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ);

        int tile = chunk.getTileAt(x,y,z);
        float[] uvFace;

        //North
        if(!Tile.Tiles.get(World.getWorld().getTileAt((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z+1)).isOpaque())
        {
            verts = getFaceVertices(0, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, 0, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

            /*for (int i = 0; i < uvListXZ.length; i++)
            {
                uvs.add(uvListXZ[i]);
            }*/
        }
        //east
        if(!Tile.Tiles.get(World.getWorld().getTileAt((int)chunk.getPosX() + x+1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x+1,y,z) == 0)
        {
            verts = getFaceVertices(1, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, 1, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //south
        if(!Tile.Tiles.get(World.getWorld().getTileAt((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z-1)).isOpaque())//chunk.getTileAt(x,y,z-1) == 0)
        {
            verts = getFaceVertices(2, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, 2, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //west
        if(!Tile.Tiles.get(World.getWorld().getTileAt((int)chunk.getPosX() + x-1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x-1,y,z) == 0)
        {
            verts = getFaceVertices(3, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, 3, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //top
        if(!Tile.Tiles.get(World.getWorld().getTileAt((int)chunk.getPosX() + x, (int)chunk.getPosY() + y+1,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x,y+1,z) == 0)
        {
            verts = getFaceVertices(4, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, 4, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //bottom
        if(!Tile.Tiles.get(World.getWorld().getTileAt((int)chunk.getPosX() + x, (int)chunk.getPosY() + y-1,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x,y-1,z) == 0)
        {
            verts = getFaceVertices(5, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, 5, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
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

    private static int[][] faceVertices = //fixed to rotate counterclockwise such that face culling works correctly
    {
            new int[] { 6,2,3,7 },//north
            new int[] { 7,3,1,5 },//east
            new int[] { 5,1,0,4 },//south
            new int[] { 4,0,2,6 },//west
            new int[] { 7,5,4,6 },//top
            new int[] { 2,0,1,3 },//bottom
    };

    private static int[] indexList =
            {
                    0,1,3,
                    3,1,2
            };
    /*
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
                    0,1,//Tile.TILE_HEIGHT,
                    1,1,//Tile.TILE_HEIGHT,
                    1,0
            };*/


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

    public static final int TERRAIN_TEXTURE_ROWS = 16;
    public static final int TERRAIN_TEXTURE_COLS = 16;

    private float tileTextureWidth = 1f / TERRAIN_TEXTURE_ROWS;
    private float tileTextureHeight = 1f / TERRAIN_TEXTURE_COLS;

    private float[] getFaceUVs(boolean isY, int face, int id)
    {
        //System.out.println(id + " " + face);
        int row = Tile.Tiles.get(id).getTextureOffset(face) % TERRAIN_TEXTURE_ROWS;
        int col = Tile.Tiles.get(id).getTextureOffset(face) / TERRAIN_TEXTURE_COLS;
        //System.out.println("Col: " + col + ", Row: " + row);


        float r0 = row * tileTextureWidth;
        float r1 = row * tileTextureWidth + tileTextureWidth;
        float c0 = col * tileTextureHeight;
        float c1 = col * tileTextureHeight + tileTextureHeight;

        return new float[]
                {
                        r0,c0,
                        r0,c1,
                        r1,c1,
                        r1,c0
                };
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
