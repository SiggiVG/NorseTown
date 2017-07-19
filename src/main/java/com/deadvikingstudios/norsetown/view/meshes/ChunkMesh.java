package com.deadvikingstudios.norsetown.view.meshes;

import com.deadvikingstudios.norsetown.model.ArrayUtils;
import com.deadvikingstudios.norsetown.model.EnumTileShape;
import com.deadvikingstudios.norsetown.model.EnumTileFace;
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
        return chunk.getChunkPosition();
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
                    EnumTileShape renderType = Tile.Tiles.get(chunk.getTile(i, j, k)).getRenderType(chunk.getMetadata(i,j,k));
                    if(renderType != EnumTileShape.NULL && renderType.isCuboid())
                    {
                        createCuboid(vertices, indices, uvs, i,j,k);
                    }
                    if(renderType.isCross())
                    {
                        createCross(vertices, indices, uvs, i,j,k);
                    }


                }
            }

        }
    }

    private void createCuboid(List<Float> vertices, List<Integer> indices, List<Float> uvs, int x, int y, int z)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + World.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ);

        Tile thisTile = Tile.Tiles.get(chunk.getTile(x, y, z));
        int thisMetadata = chunk.getTile(x, y, z);
        EnumTileShape thisTileShape = thisTile.getRenderType(thisMetadata);
        //System.out.println(thisTileShape);
        float[] uvFace;

        Tile tileCheck;
        int tileCheckMeta;
        EnumTileShape tileShapeCheck;

        float ts = Tile.TILE_SIZE;
        float th = Tile.TILE_HEIGHT;

        //change thicknesses
        if(thisTileShape == EnumTileShape.COL_THICK)
        {
            ts = Tile.TILE_SIZE * 0.75f;
        }
        else if(thisTileShape == EnumTileShape.COL_MED)
        {
            ts = Tile.TILE_SIZE *0.5f;
        }
        else if(thisTileShape == EnumTileShape.COL_THIN)
        {
            ts = Tile.TILE_SIZE * 0.25f;
        }

        //recenter
        if(thisTileShape.isCenteredColumn())
        {
            vec.translate((Tile.TILE_SIZE-ts)*0.5f, 0, (Tile.TILE_SIZE-ts)*0.5f);
        }

        //NORTH
        tileCheck = Tile.Tiles.get(World.getWorld().getTile((int) chunk.getPosX() + x, (int) chunk.getPosY() + y, (int) chunk.getPosZ() + z+1));
        tileCheckMeta = (World.getWorld().getMetadata((int) chunk.getPosX() + x, (int) chunk.getPosY() + y, (int) chunk.getPosZ() + z+1));
        tileShapeCheck = tileCheck.getRenderType(tileCheckMeta);

        if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.NORTH, EnumTileFace.SOUTH) || tileShapeCheck == EnumTileShape.FULL_CUBE)))
        {
            verts = getFaceVerticesCuboid(EnumTileFace.NORTH, vec, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.NORTH, thisTile.getIndex());
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }//END NORTH

        //EAST
        tileCheck = Tile.Tiles.get(World.getWorld().getTile((int) chunk.getPosX() + x+1, (int) chunk.getPosY() + y, (int) chunk.getPosZ() + z));
        tileCheckMeta = (World.getWorld().getMetadata((int) chunk.getPosX() + x+1, (int) chunk.getPosY() + y, (int) chunk.getPosZ() + z));
        tileShapeCheck = tileCheck.getRenderType(tileCheckMeta);

        if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.EAST, EnumTileFace.WEST) || tileShapeCheck == EnumTileShape.FULL_CUBE)))
        {
            verts = getFaceVerticesCuboid(EnumTileFace.EAST, vec, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.EAST, thisTile.getIndex());
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }//END EAST

        //SOUTH
        tileCheck = Tile.Tiles.get(World.getWorld().getTile((int) chunk.getPosX() + x, (int) chunk.getPosY() + y, (int) chunk.getPosZ() + z-1));
        tileCheckMeta = (World.getWorld().getMetadata((int) chunk.getPosX() + x, (int) chunk.getPosY() + y, (int) chunk.getPosZ() + z-1));
        tileShapeCheck = tileCheck.getRenderType(tileCheckMeta);

        if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.SOUTH, EnumTileFace.NORTH) || tileShapeCheck == EnumTileShape.FULL_CUBE)))
        {
            verts = getFaceVerticesCuboid(EnumTileFace.SOUTH, vec, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.SOUTH, thisTile.getIndex());
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }//END SOUTH

        //WEST
        tileCheck = Tile.Tiles.get(World.getWorld().getTile((int) chunk.getPosX() + x-1, (int) chunk.getPosY() + y, (int) chunk.getPosZ() + z));
        tileCheckMeta = (World.getWorld().getMetadata((int) chunk.getPosX() + x-1, (int) chunk.getPosY() + y, (int) chunk.getPosZ() + z));
        tileShapeCheck = tileCheck.getRenderType(tileCheckMeta);

        if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.WEST, EnumTileFace.EAST) || tileShapeCheck == EnumTileShape.FULL_CUBE)))
        {
            verts = getFaceVerticesCuboid(EnumTileFace.WEST, vec, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.WEST, thisTile.getIndex());
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }//END WEST

        //TOP
        tileCheck = Tile.Tiles.get(World.getWorld().getTile((int) chunk.getPosX() + x, (int) chunk.getPosY() + y + 1, (int) chunk.getPosZ() + z));
        tileCheckMeta = (World.getWorld().getMetadata((int) chunk.getPosX() + x, (int) chunk.getPosY() + y + 1, (int) chunk.getPosZ() + z));
        tileShapeCheck = tileCheck.getRenderType(tileCheckMeta);

        if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.TOP, EnumTileFace.BOTTOM) || tileShapeCheck == EnumTileShape.FULL_CUBE)))
        {
            verts = getFaceVerticesCuboid(EnumTileFace.TOP, vec, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.TOP, thisTile.getIndex());
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }//END TOP

        //BOTTOM
        tileCheck = Tile.Tiles.get(World.getWorld().getTile((int) chunk.getPosX() + x, (int) chunk.getPosY() + y - 1, (int) chunk.getPosZ() + z));
        tileCheckMeta = (World.getWorld().getMetadata((int) chunk.getPosX() + x, (int) chunk.getPosY() + y - 1, (int) chunk.getPosZ() + z));
        tileShapeCheck = tileCheck.getRenderType(tileCheckMeta);

        if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.BOTTOM, EnumTileFace.TOP) || tileShapeCheck == EnumTileShape.FULL_CUBE)))
        {
            verts = getFaceVerticesCuboid(EnumTileFace.BOTTOM, vec, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.BOTTOM, thisTile.getIndex());
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }//END BOTTOM
    }

    private static float[] getFaceVerticesCuboid(EnumTileFace face, Vector3f vec, float tileSize, float tileHeight)
    {
        float[] fv = new float[4*3];
        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = (vertexCuboidList[faceVertices[face.ordinal()][i]][0] * tileSize) + vec.x;
            fv[i*3+1] = (vertexCuboidList[faceVertices[face.ordinal()][i]][1] * tileHeight) + vec.y;
            fv[i*3+2] = (vertexCuboidList[faceVertices[face.ordinal()][i]][2] * tileSize) + vec.z;
        }
        return fv;
    }

    private void createCross(List<Float> vertices, List<Integer> indices, List<Float> uvs, int x, int y, int z)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + World.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ);

        int tile = chunk.getTile(x,y,z);
        float[] uvFace;

        for (int j = 0; j < 4; j++)
        {
            verts = getFaceVerticesCross(EnumTileFace.get(j), vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));
            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.get(j), tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }

    }

    private static int[][] faceVertices = //fixed to rotate counterclockwise such that face culling works correctly
            {
                    new int[] { 6,2,3,7 },//north
                    new int[] { 7,3,1,5 },//east
                    new int[] { 5,1,0,4 },//south
                    new int[] { 4,0,2,6 },//west
                    new int[] { 7,5,4,6 },//top
                    new int[] { 2,0,1,3 },//bottom
            };

    private static float[][] vertexCuboidList =
            {
                    new float[]{0, 0, 0}, //0
                    new float[]{1, 0, 0}, //1
                    new float[]{0, 0, 1}, //2
                    new float[]{1, 0, 1}, //3

                    new float[]{0, 1, 0}, //4
                    new float[]{1, 1, 0}, //5
                    new float[]{0, 1, 1}, //6
                    new float[]{1, 1, 1}  //7
            };

    private static int[] indexList =
            {
                    0,1,3,
                    3,1,2
            };

    private float[] getFaceUVs(boolean isY, EnumTileFace face, int id)
    {
        //System.out.println(id + " " + face);
        int row = Tile.Tiles.get(id).getTextureOffset(face.ordinal()) % TERRAIN_TEXTURE_ROWS;
        int col = Tile.Tiles.get(id).getTextureOffset(face.ordinal()) / TERRAIN_TEXTURE_COLS;
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



    private static float[] getFaceVerticesCross(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];

        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexCrossList[faceVerticesCross[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexCrossList[faceVerticesCross[face.ordinal()][i]][1] + vec.y;
            fv[i*3+2] = vertexCrossList[faceVerticesCross[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }

    private static float diag0 = ((float)Math.sqrt((Tile.TILE_SIZE*Tile.TILE_SIZE)+(Tile.TILE_SIZE*Tile.TILE_SIZE))-Tile.TILE_SIZE)*0.5f;
    private static float diag1 = Tile.TILE_SIZE - ((float)Math.sqrt((Tile.TILE_SIZE*Tile.TILE_SIZE)+(Tile.TILE_SIZE*Tile.TILE_SIZE))-Tile.TILE_SIZE)*0.5f;

    private static float[][] vertexCrossList =
            {
                    new float[]{ diag0,0,diag0}, //0
                    new float[]{ diag1,0,diag0}, //1
                    new float[]{ diag0,0,diag1}, //2
                    new float[]{ diag1,0,diag1}, //3

                    new float[]{ diag0,Tile.TILE_HEIGHT,diag0}, //4
                    new float[]{ diag1,Tile.TILE_HEIGHT,diag0}, //5
                    new float[]{ diag0,Tile.TILE_HEIGHT,diag1}, //6
                    new float[]{ diag1,Tile.TILE_HEIGHT,diag1}  //7
            };

    private static int[][] faceVerticesCross = //fixed to rotate counterclockwise such that face culling works correctly
            {
                    new int[] { 4,0,3,7 },//'/' front
                    new int[] { 7,3,0,4 },//'/' back
                    new int[] { 5,1,2,6 },//'\' front
                    new int[] { 6,2,1,5 },//'\' back
            };

    public static final int TERRAIN_TEXTURE_ROWS = 16;
    public static final int TERRAIN_TEXTURE_COLS = 16;

    private float tileTextureWidth = 1f / TERRAIN_TEXTURE_ROWS;
    private float tileTextureHeight = 1f / TERRAIN_TEXTURE_COLS;

    public void reloadMesh()
    {
        //System.out.println("Chunk " + (int)this.getChunkAtIndex().getPosX() / Chunk.CHUNK_SIZE + "," + (int)this.getChunkAtIndex().getPosY()/ Chunk.CHUNK_HEIGHT + "," + (int)this.getChunkAtIndex().getPosZ()/ Chunk.CHUNK_SIZE + " has had its mesh reloaded.");

        List<Float> vertices = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();
        List<Float> uvs = new ArrayList<Float>();

        createMesh(vertices, indices, uvs);

        rawMesh = loader.reloadToVAO(rawMesh, ArrayUtils.floatFromFloat(vertices),
                ArrayUtils.intFromInteger(indices), ArrayUtils.floatFromFloat(uvs));
    }





        /*
        //NORTH
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z+1)).isOpaque())
        {
            verts = getFaceVerticesFullCube(EnumTileFace.NORTH, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.NORTH, thisTile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

            /*for (int i = 0; i < uvListXZ.length; i++)
            {
                uvs.add(uvListXZ[i]);
            }
        }
        //east
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x+1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x+1,y,z) == 0)
        {
            verts = getFaceVerticesFullCube(EnumTileFace.EAST, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.EAST, thisTile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //south
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z-1)).isOpaque())//chunk.getTileAt(x,y,z-1) == 0)
        {
            verts = getFaceVerticesFullCube(EnumTileFace.SOUTH, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.SOUTH, thisTile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //west
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x-1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x-1,y,z) == 0)
        {
            verts = getFaceVerticesFullCube(EnumTileFace.WEST, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.WEST, thisTile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //top
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y+1,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x,y+1,z) == 0)
        {
            verts = getFaceVerticesFullCube(EnumTileFace.TOP, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.TOP, thisTile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //bottom
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y-1,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x,y-1,z) == 0)
        {
            verts = getFaceVerticesFullCube(EnumTileFace.BOTTOM, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.BOTTOM, thisTile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }

    }*/
    /*
    private void createColumnThick(List<Float> vertices, List<Integer> indices, List<Float> uvs, int x, int y, int z)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + World.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ);

        int tile = chunk.getTile(x,y,z);
        float[] uvFace;

        //NORTH
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z+1)).isOpaque())
        {
            verts = getFaceVerticesColumnThick(EnumTileFace.NORTH, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.NORTH, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //east
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x+1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x+1,y,z) == 0)
        {
            verts = getFaceVerticesColumnThick(EnumTileFace.EAST, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.EAST, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //south
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z-1)).isOpaque())//chunk.getTileAt(x,y,z-1) == 0)
        {
            verts = getFaceVerticesColumnThick(EnumTileFace.SOUTH, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.SOUTH, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //west
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x-1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x-1,y,z) == 0)
        {
            verts = getFaceVerticesColumnThick(EnumTileFace.WEST, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.WEST, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        int tileUp = (World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y+1,(int)chunk.getPosZ() + z));
        int tileUpMeta = (World.getWorld().getMetadata((int)chunk.getPosX() + x, (int)chunk.getPosY() + y+1,(int)chunk.getPosZ() + z));
        EnumTileShape renderTypeUp = Tile.Tiles.get(tileUp).getRenderType(tileUpMeta);
        //top
        if(!Tile.Tiles.get(tileUp).isOpaque() && (renderTypeUp > 1 || renderTypeUp == -1))
        {
            verts = getFaceVerticesColumnThick(EnumTileFace.TOP, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.TOP, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        int tileDown = (World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y-1,(int)chunk.getPosZ() + z));
        int tileDownMeta = (World.getWorld().getMetadata((int)chunk.getPosX() + x, (int)chunk.getPosY() + y-1,(int)chunk.getPosZ() + z));
        EnumTileShape renderTypeDown = Tile.Tiles.get(tileDown).getRenderType(tileDownMeta);
        //bottom
        if(!Tile.Tiles.get(tileDown).isOpaque() && (renderTypeDown > 1 || renderTypeDown == -1))
        {
            verts = getFaceVerticesColumnThick(EnumTileFace.BOTTOM, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.BOTTOM, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }

    }

    private void createColumnMedium(List<Float> vertices, List<Integer> indices, List<Float> uvs, int x, int y, int z)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + World.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ);

        int tile = chunk.getTile(x,y,z);
        float[] uvFace;

        //NORTH
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z+1)).isOpaque())
        {
            verts = getFaceVerticesColumnMedium(EnumTileFace.NORTH, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.NORTH, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //east
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x+1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x+1,y,z) == 0)
        {
            verts = getFaceVerticesColumnMedium(EnumTileFace.EAST, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.EAST, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //south
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z-1)).isOpaque())//chunk.getTileAt(x,y,z-1) == 0)
        {
            verts = getFaceVerticesColumnMedium(EnumTileFace.SOUTH, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.SOUTH, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //west
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x-1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x-1,y,z) == 0)
        {
            verts = getFaceVerticesColumnMedium(EnumTileFace.WEST, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.WEST, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        int tileUp = (World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y+1,(int)chunk.getPosZ() + z));
        int tileUpMeta = (World.getWorld().getMetadata((int)chunk.getPosX() + x, (int)chunk.getPosY() + y+1,(int)chunk.getPosZ() + z));
        int renderTypeUp = Tile.Tiles.get(tileUp).getRenderType(tileUpMeta);
        //top
        if(!Tile.Tiles.get(tileUp).isOpaque() && (renderTypeUp > 2 || renderTypeUp == -1))
        {
            verts = getFaceVerticesColumnMedium(EnumTileFace.TOP, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.TOP, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        int tileDown = (World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y-1,(int)chunk.getPosZ() + z));
        int tileDownMeta = (World.getWorld().getMetadata((int)chunk.getPosX() + x, (int)chunk.getPosY() + y-1,(int)chunk.getPosZ() + z));
        int renderTypeDown = Tile.Tiles.get(tileDown).getRenderType(tileDownMeta);
        //bottom
        if(!Tile.Tiles.get(tileDown).isOpaque() && (renderTypeDown > 2 || renderTypeDown == -1))
        {
            verts = getFaceVerticesColumnMedium(EnumTileFace.BOTTOM, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.BOTTOM, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }

    }

    private void createColumnThin(List<Float> vertices, List<Integer> indices, List<Float> uvs, int x, int y, int z)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + World.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + World.CHUNK_OFFSET_XZ);

        int tile = chunk.getTile(x,y,z);
        float[] uvFace;

        //NORTH
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z+1)).isOpaque())
        {
            verts = getFaceVerticesColumnThin(EnumTileFace.NORTH, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.NORTH, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //east
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x+1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x+1,y,z) == 0)
        {
            verts = getFaceVerticesColumnThin(EnumTileFace.EAST, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.EAST, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //south
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z-1)).isOpaque())//chunk.getTileAt(x,y,z-1) == 0)
        {
            verts = getFaceVerticesColumnThin(EnumTileFace.SOUTH, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.SOUTH, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        //west
        if(!Tile.Tiles.get(World.getWorld().getTile((int)chunk.getPosX() + x-1, (int)chunk.getPosY() + y,(int)chunk.getPosZ() + z)).isOpaque())//chunk.getTileAt(x-1,y,z) == 0)
        {
            verts = getFaceVerticesColumnThin(EnumTileFace.WEST, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.SOUTH, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        int tileUp = (World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y+1,(int)chunk.getPosZ() + z));
        int tileUpMeta = (World.getWorld().getMetadata((int)chunk.getPosX() + x, (int)chunk.getPosY() + y+1,(int)chunk.getPosZ() + z));
        int renderTypeUp = Tile.Tiles.get(tileUp).getRenderType(tileUpMeta);
        //top
        if(!Tile.Tiles.get(tileUp).isOpaque() && (renderTypeUp > 3 || renderTypeUp == -1))
        {
            verts = getFaceVerticesColumnThin(EnumTileFace.TOP, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.TOP, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        int tileDown = (World.getWorld().getTile((int)chunk.getPosX() + x, (int)chunk.getPosY() + y-1,(int)chunk.getPosZ() + z));
        int tileDownMeta = (World.getWorld().getMetadata((int)chunk.getPosX() + x, (int)chunk.getPosY() + y-1,(int)chunk.getPosZ() + z));
        int renderTypeDown = Tile.Tiles.get(tileDown).getRenderType(tileDownMeta);
        //bottom
        if(!Tile.Tiles.get(tileDown).isOpaque() && (renderTypeDown > 3 || renderTypeDown == -1))
        {
            verts = getFaceVerticesColumnThin(EnumTileFace.BOTTOM, vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.BOTTOM, tile);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }

    }*/

    /*private static float[][] vertexFullCubeList =
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

    };

    private static float colThick0 = Tile.TILE_SIZE/8;
    private static float colThick1 = Tile.TILE_SIZE - Tile.TILE_SIZE/8;

    private static float[][] vertexColumnThickList =
            {
                    new float[]{colThick0,          0, colThick0}, //0
                    new float[]{colThick1,          0, colThick0}, //1
                    new float[]{colThick0,          0, colThick1}, //2
                    new float[]{colThick1,          0, colThick1}, //3

                    new float[]{colThick0,          Tile.TILE_HEIGHT, colThick0}, //4
                    new float[]{colThick1,          Tile.TILE_HEIGHT, colThick0}, //5
                    new float[]{colThick0,          Tile.TILE_HEIGHT, colThick1}, //6
                    new float[]{colThick1,          Tile.TILE_HEIGHT, colThick1}  //7
            };

    private static float colMedium0 = Tile.TILE_SIZE/4;
    private static float colMedium1 = Tile.TILE_SIZE - Tile.TILE_SIZE/4;

    private static float[][] vertexColumnMediumList =
            {
                    new float[]{colMedium0,          0, colMedium0}, //0
                    new float[]{colMedium1,          0, colMedium0}, //1
                    new float[]{colMedium0,          0, colMedium1}, //2
                    new float[]{colMedium1,          0, colMedium1}, //3

                    new float[]{colMedium0,          Tile.TILE_HEIGHT, colMedium0}, //4
                    new float[]{colMedium1,          Tile.TILE_HEIGHT, colMedium0}, //5
                    new float[]{colMedium0,          Tile.TILE_HEIGHT, colMedium1}, //6
                    new float[]{colMedium1,          Tile.TILE_HEIGHT, colMedium1}  //7
            };


    private static float colThin0 = Tile.TILE_SIZE/3;
    private static float colThin1 = Tile.TILE_SIZE - Tile.TILE_SIZE/3;

    private static float[][] vertexColumnThinList =
            {
                    new float[]{colThin0,          0, colThin0}, //0
                    new float[]{colThin1,          0, colThin0}, //1
                    new float[]{colThin0,          0, colThin1}, //2
                    new float[]{colThin1,          0, colThin1}, //3

                    new float[]{colThin0,          Tile.TILE_HEIGHT, colThin0}, //4
                    new float[]{colThin1,          Tile.TILE_HEIGHT, colThin0}, //5
                    new float[]{colThin0,          Tile.TILE_HEIGHT, colThin1}, //6
                    new float[]{colThin1,          Tile.TILE_HEIGHT, colThin1}  //7
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
                    0,1,//Tile.TILE_HEIGHT,
                    1,1,//Tile.TILE_HEIGHT,
                    1,0
            };*/
    /*
    private static float[] getFaceVerticesFullCube(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];
        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexFullCubeList[faceVertices[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexFullCubeList[faceVertices[face.ordinal()][i]][1] + vec.y;
            fv[i*3+2] = vertexFullCubeList[faceVertices[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }

    private static float[] getFaceVerticesColumnThick(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];
        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexColumnThickList[faceVertices[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexColumnThickList[faceVertices[face.ordinal()][i]][1] + vec.y;
            fv[i*3+2] = vertexColumnThickList[faceVertices[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }

    private static float[] getFaceVerticesColumnMedium(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];
        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexColumnMediumList[faceVertices[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexColumnMediumList[faceVertices[face.ordinal()][i]][1] + vec.y;
            fv[i*3+2] = vertexColumnMediumList[faceVertices[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }

    private static float[] getFaceVerticesColumnThin(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];
        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexColumnThinList[faceVertices[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexColumnThinList[faceVertices[face.ordinal()][i]][1] + vec.y;
            fv[i*3+2] = vertexColumnThinList[faceVertices[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }



    */
}
