package com.deadvikingstudios.norsetown.view.meshes;

import com.deadvikingstudios.norsetown.model.tiles.EnumTileFace;
import com.deadvikingstudios.norsetown.model.tiles.EnumTileShape;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.WorldOld;
import com.deadvikingstudios.norsetown.model.world.structures.Chunk;
import com.deadvikingstudios.norsetown.model.world.structures.ChunkColumn;
import com.deadvikingstudios.norsetown.utils.ArrayUtils;
import com.deadvikingstudios.norsetown.utils.Logger;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.deadvikingstudios.norsetown.controller.GameContainer.loader;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * This is how ChunksColumns Render, and gets updated whenever a chunk does
 *
 * TODO: move some code around so it's easier to follow.
 */
public class StructureMesh extends EntityMesh
{
    public StructureMesh(ChunkColumn col, MeshTexture texture)
    {
        super(col, texture);

        List<Float> vertices = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();
        List<Float> uvs = new ArrayList<Float>();
        List<Float> norms = new ArrayList<Float>();

        createMesh(vertices, indices, uvs, norms);

        rawMesh = loader.loadToVAO(ArrayUtils.floatFromFloat(vertices),
                ArrayUtils.intFromInteger(indices), ArrayUtils.floatFromFloat(uvs), ArrayUtils.floatFromFloat(norms));
    }

    public Vector3f getPosition()
    {
        return ((ChunkColumn)entity).getPosition();
    }

    public ChunkColumn getChunkColumn()
    {
        return ((ChunkColumn)entity);
    }

//    private boolean checkNeighborIsAir(int xIn, int yIn, int zIn, int xOff, int yOff, int zOff)
//    {
//        if(xOff != 0)
//        {
//            //X is within chunk
//            if(xIn+xOff >= 0 && xIn+xOff < Chunk.SIZE)
//            {
//                return this.getChunkColumn().getTile(xIn+xOff, yIn, zIn).isAir();
//            }
//            else //X is outside chunk
//            {
//                return this.getChunkColumn().getStructure().getTile(xIn+xOff, yIn, zIn).isAir();
//            }
//        }
//        else if(yOff != 0)
//        {
//            return this.getChunkColumn().getTile(xIn, yIn+yOff, zIn).isAir();
//        }
//        else if(zOff != 0)
//        {
//            //Z is within chunk
//            if(zIn+zOff >= 0 && zIn+zOff < Chunk.SIZE)
//            {
//                return this.getChunkColumn().getTile(xIn, yIn, zIn+zOff).isAir();
//            }
//            else //Z is outside chunk
//            {
//                return this.getChunkColumn().getStructure().getTile(xIn, yIn, zIn+xOff).isAir();
//            }
//        }
//        else return true;
//    }

    protected void createMesh(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms)
    {
        Logger.debug("Generating new Mesh for " + this.getChunkColumn().position.x + "," + this.getChunkColumn().position.y);
        for (Map.Entry<Integer, Chunk> entry : getChunkColumn().getChunks().entrySet())
        {
            Chunk chunk = entry.getValue();
            int x = chunk.getRenderPosition().x + (this.getChunkColumn().position.x * Chunk.SIZE);// - this.getEntityStructure().getStructureOffset().x;
            int y = chunk.getRenderPosition().y;// - this.getEntityStructure().getStructureOffset().y;
            int z = chunk.getRenderPosition().z + (this.getChunkColumn().position.y * Chunk.SIZE);// - this.getEntityStructure().getStructureOffset().y;
            //Logger.debug("Mesh created for " + x + "," + y + "," + y);

            int m = this.getChunkColumn().getStructure().getPosition().x;
            int n = this.getChunkColumn().getStructure().getPosition().y;
            int o = this.getChunkColumn().getStructure().getPosition().z;

            for (byte i = 0; i < Chunk.SIZE; ++i)
            {
                for (byte j = 0; j < Chunk.SIZE; ++j)
                {
                    for (byte k = 0; k < Chunk.SIZE; ++k)
                    {
                        Tile tile = this.getChunkColumn().getTile(i, y + j, k);
                        EnumTileShape tileShape = tile.getTileShape(0);

//                        EnumTileShape tileShape = tile.getTileShape(this.get().getMetadata(i,j,k));
//                        int thisMetadata = getChunk().getTile(i,j,k);
                        /*if(tile == Tile.Tiles.tileGrass && Tile.Tiles.get(chunk.getTile(i, j+1, k)).isAir())
                        {
                            createFullCross(vertices, indices, uvs, i, j+1, k, true);
                        }*/
                        if (tileShape == EnumTileShape.NULL)
                        {
                            continue;
                        }
                        if (tileShape.isCuboid())
                        {
                            createCuboid(vertices, indices, uvs, norms, tile, x+i+m, y+j+n, z+k+o);
                        }
                        if (tileShape.isCross())
                        {

                            if (tileShape == EnumTileShape.CUBE_CROSS_EXTENDED)
                            {
                                //createExtendedCrossForCube(vertices, indices, uvs, norms, tile, i,j,k);
                                createExtendedCross(vertices, indices, uvs, norms, tile, x+i+m, y+j+n, z+k+o, 0);
                            } else if (tileShape == EnumTileShape.CROSS_EXTENDED)
                            {
                                createExtendedCross(vertices, indices, uvs, norms, tile, x+i+m, y+j+n, z+k+o, 0);
                            } else if (tileShape == EnumTileShape.CROSS_FULL)
                            {
                                createFullCross(vertices, indices, uvs, norms, tile, x+i+m, y+j+n, z+k+o, 0);
                            } else if (tileShape == EnumTileShape.CROSS_TALL)
                            {
                                createTallCross(vertices, indices, uvs, norms, tile, x+i+m, y+j+n, z+k+o, 0);
                            } else if (tileShape == EnumTileShape.CROSS_TALL_FULL)
                            {
                                createTallFullCross(vertices, indices, uvs, norms, tile, x+i+m, y+j+n, z+k+o, 0);
                            } else
                            {
                                createCross(vertices, indices, uvs, norms, tile, x+i+m, y+j+n, z+k+o, 0);
                            }
                        }
                        if (tile == Tile.Tiles.tileGrass && this.getChunkColumn().getTile(i, y + j + 1, k).isAir())
                        {
                            createFullCross(vertices, indices, uvs, norms, tile, x+i+m, y+j+n+1, z+k+o, 0);
                        }
                    }
                }
            }
        }
    }

    private void createCuboid(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms, Tile thisTile, int x, int y, int z)
    {
        float[] verts;

        //support for when all tiles have sides that do not = 1;
        Vector3f origin = new Vector3f(x * Tile.TILE_SIZE, y * Tile.TILE_HEIGHT, z * Tile.TILE_SIZE);
        //makes the negative chunks render in the correct location.
//        if(vec.y < 0) vec.y += 1;

        EnumTileShape thisTileShape = thisTile.getTileShape(0);
        //System.out.println(thisTileShape);
        float[] uvFace;
        float[] normFace;

        Tile tileCheck;
        int tileCheckMeta;
        EnumTileShape tileShapeCheck;

        float ts = Tile.TILE_SIZE;
        float th = Tile.TILE_HEIGHT;

        //change thicknesses
//        if(thisTileShape == EnumTileShape.COL_THICK)
//        {
//            ts = Tile.TILE_SIZE * 0.75f;
//        }
//        else if(thisTileShape == EnumTileShape.COL_MED)
//        {
//            ts = Tile.TILE_SIZE *0.5f;
//        }
//        else if(thisTileShape == EnumTileShape.COL_THIN)
//        {
//            ts = Tile.TILE_SIZE * 0.25f;
//        }
//
//        //recenter
//        if(thisTileShape.isCenteredColumn())
//        {
//            origin.translate((Tile.TILE_SIZE-ts)*0.5f, 0, (Tile.TILE_SIZE-ts)*0.5f);
//        }

        //NORTH
        tileCheck = this.getChunkColumn().getStructure().getTile(x,y,z+1);
        tileShapeCheck = tileCheck.getTileShape(0);
//        tileCheck = Tile.Tiles.get(WorldOld.getWorld().getTile((int)getChunk().getPosX()+x,(int)getChunk().getPosY()+y,(int)getChunk().getPosZ()+y+1));
//        tileCheckMeta = (WorldOld.getWorld().getMetadata((int) getChunk().getPosX() + x, (int) getChunk().getPosY() + y, (int) getChunk().getPosZ() + y+1));
//        tileShapeCheck = tileCheck.getTileShape(tileCheckMeta);
//
        if(!tileCheck.isOpaque() || thisTileShape.renderThisFace(EnumTileFace.NORTH, tileShapeCheck))
                //&& ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.NORTH, EnumTileFace.SOUTH) || tileShapeCheck == EnumTileShape.FULL_CUBE)))
        {
            verts = getFaceVerticesCuboid(EnumTileFace.NORTH, origin, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.NORTH, thisTile.getIndex(), 0);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

            normFace = getCuboidFaceNormals(EnumTileFace.NORTH);
            for (int i = 0; i < normFace.length; i++)
            {
                norms.add(normFace[i]);
            }


        }//END NORTH

        //EAST
        tileCheck = this.getChunkColumn().getStructure().getTile(x+1,y,z);
        tileShapeCheck = tileCheck.getTileShape(0);
//        tileCheck = Tile.Tiles.get(WorldOld.getWorld().getTile((int) getChunk().getPosX() + x+1, (int) getChunk().getPosY() + y, (int) getChunk().getPosZ() + y));
//        tileCheckMeta = (WorldOld.getWorld().getMetadata((int) getChunk().getPosX() + x+1, (int) getChunk().getPosY() + y, (int) getChunk().getPosZ() + y));
//        tileShapeCheck = tileCheck.getTileShape(tileCheckMeta);
//
        if(!tileCheck.isOpaque() || thisTileShape.renderThisFace(EnumTileFace.EAST, tileShapeCheck))
        /*if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.EAST, EnumTileFace.WEST) || tileShapeCheck == EnumTileShape.FULL_CUBE)))*/
        {
            verts = getFaceVerticesCuboid(EnumTileFace.EAST, origin, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.EAST, thisTile.getIndex(), 0);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

            normFace = getCuboidFaceNormals(EnumTileFace.EAST);
            for (int i = 0; i < normFace.length; i++)
            {
                norms.add(normFace[i]);
            }
        }//END EAST

        //SOUTH
        tileCheck = this.getChunkColumn().getStructure().getTile(x,y,z-1);
        tileShapeCheck = tileCheck.getTileShape(0);
//        tileCheck = Tile.Tiles.get(WorldOld.getWorld().getTile((int) getChunk().getPosX() + x, (int) getChunk().getPosY() + y, (int) getChunk().getPosZ() + y-1));
//        tileCheckMeta = (WorldOld.getWorld().getMetadata((int) getChunk().getPosX() + x, (int) getChunk().getPosY() + y, (int) getChunk().getPosZ() + y-1));
//        tileShapeCheck = tileCheck.getTileShape(tileCheckMeta);
//
        if(!tileCheck.isOpaque() || thisTileShape.renderThisFace(EnumTileFace.SOUTH, tileShapeCheck))
        /*if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.SOUTH, EnumTileFace.NORTH) || tileShapeCheck == EnumTileShape.FULL_CUBE)))*/
        {
            verts = getFaceVerticesCuboid(EnumTileFace.SOUTH, origin, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.SOUTH, thisTile.getIndex(), 0);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

            normFace = getCuboidFaceNormals(EnumTileFace.SOUTH);
            for (int i = 0; i < normFace.length; i++)
            {
                norms.add(normFace[i]);
            }
        }//END SOUTH

        //WEST
        tileCheck = this.getChunkColumn().getStructure().getTile(x-1,y,z);
        tileShapeCheck = tileCheck.getTileShape(0);
//        tileCheck = Tile.Tiles.get(WorldOld.getWorld().getTile((int) getChunk().getPosX() + x-1, (int) getChunk().getPosY() + y, (int) getChunk().getPosZ() + y));
//        tileCheckMeta = (WorldOld.getWorld().getMetadata((int) getChunk().getPosX() + x-1, (int) getChunk().getPosY() + y, (int) getChunk().getPosZ() + y));
//        tileShapeCheck = tileCheck.getTileShape(tileCheckMeta);
//
        if(!tileCheck.isOpaque() || thisTileShape.renderThisFace(EnumTileFace.WEST, tileShapeCheck))
        /*if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.WEST, EnumTileFace.EAST) || tileShapeCheck == EnumTileShape.FULL_CUBE)))*/
        {
            verts = getFaceVerticesCuboid(EnumTileFace.WEST, origin, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.WEST, thisTile.getIndex(), 0);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

            normFace = getCuboidFaceNormals(EnumTileFace.WEST);
            for (int i = 0; i < normFace.length; i++)
            {
                norms.add(normFace[i]);
            }
        }//END WEST*/

        //TOP
        tileCheck = this.getChunkColumn().getStructure().getTile(x,y+1,z);
        tileShapeCheck = tileCheck.getTileShape(0);
//        tileCheck = Tile.Tiles.get(WorldOld.getWorld().getTile((int)getChunk().getPosX() + x, (int)getChunk().getPosY() + y + 1, (int)getChunk().getPosZ() + y));
//        tileCheckMeta = (WorldOld.getWorld().getMetadata((int) getChunk().getPosX() + x, (int) getChunk().getPosY() + y + 1, (int) getChunk().getPosZ() + y));
//        tileShapeCheck = tileCheck.getTileShape(tileCheckMeta);
//
        if(!tileCheck.isOpaque() || thisTileShape.renderThisFace(EnumTileFace.TOP, tileShapeCheck))
        /*if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.TOP, EnumTileFace.BOTTOM) || tileShapeCheck == EnumTileShape.FULL_CUBE)))*/
        {
            verts = getFaceVerticesCuboid(EnumTileFace.TOP, origin, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.TOP, thisTile.getIndex(), 0);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

            normFace = getCuboidFaceNormals(EnumTileFace.TOP);
            for (int i = 0; i < normFace.length; i++)
            {
                norms.add(normFace[i]);
            }
        }//END TOP

        //BOTTOM
        tileCheck = this.getChunkColumn().getStructure().getTile(x,y-1,z);
        tileShapeCheck = tileCheck.getTileShape(0);
//        tileCheck = Tile.Tiles.get(WorldOld.getWorld().getTile((int) getChunk().getPosX() + x, (int) getChunk().getPosY() + y - 1, (int) getChunk().getPosZ() + y));
//        tileCheckMeta = (WorldOld.getWorld().getMetadata((int) getChunk().getPosX() + x, (int) getChunk().getPosY() + y - 1, (int) getChunk().getPosZ() + y));
//        tileShapeCheck = tileCheck.getTileShape(tileCheckMeta);
//
//        if(vec.y > Tile.TILE_HEIGHT)
        if(!tileCheck.isOpaque() || thisTileShape.renderThisFace(EnumTileFace.BOTTOM, tileShapeCheck)  )
        /*if (((!tileCheck.isOpaque()) || tileCheck.isAir())
                && ((!thisTileShape.isOtherFaceGTEQThisFace(tileShapeCheck, EnumTileFace.BOTTOM, EnumTileFace.TOP) || tileShapeCheck == EnumTileShape.FULL_CUBE)))*/
        {
            verts = getFaceVerticesCuboid(EnumTileFace.BOTTOM, origin, ts, th);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));

            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }

            uvFace = getFaceUVs(false, EnumTileFace.BOTTOM, thisTile.getIndex(), 0);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

            normFace = getCuboidFaceNormals(EnumTileFace.BOTTOM);
            for (int i = 0; i < normFace.length; i++)
            {
                norms.add(normFace[i]);
            }
        }//END BOTTOM*/
    }

    /**
     * Creates the normals based on which face is input
     * @param face
     * @return
     */
    private static float[] getCuboidFaceNormals(EnumTileFace face)
    {
        float[] norms = new float[12];

        switch (face)
        {
            case NORTH:
            {
                norms[0] = 0;
                norms[1] = 0;
                norms[2] = 1;
                norms[3] = 0;
                norms[4] = 0;
                norms[5] = 1;
                norms[6] = 0;
                norms[7] = 0;
                norms[8] = 1;
                norms[9] = 0;
                norms[10] = 0;
                norms[11] = 1;
                break;
            }
            case EAST:
            {
                norms[0] = 1;
                norms[1] = 0;
                norms[2] = 0;
                norms[3] = 1;
                norms[4] = 0;
                norms[5] = 0;
                norms[6] = 1;
                norms[7] = 0;
                norms[8] = 0;
                norms[9] = 1;
                norms[10] = 0;
                norms[11] = 0;
                break;
            }
            case SOUTH:
            {
                norms[0] = 0;
                norms[1] = 0;
                norms[2] = -1;
                norms[3] = 0;
                norms[4] = 0;
                norms[5] = -1;
                norms[6] = 0;
                norms[7] = 0;
                norms[8] = -1;
                norms[9] = 0;
                norms[10] = 0;
                norms[11] = -1;
                break;
            }
            case WEST:
            {
                norms[0] = -1;
                norms[1] = 0;
                norms[2] = 0;
                norms[3] = -1;
                norms[4] = 0;
                norms[5] = 0;
                norms[6] = -1;
                norms[7] = 0;
                norms[8] = 0;
                norms[9] = -1;
                norms[10] = 0;
                norms[11] = 0;
                break;
            }
            case TOP:
            {
                norms[0] = 0;
                norms[1] = 1;
                norms[2] = 0;
                norms[3] = 0;
                norms[4] = 1;
                norms[5] = 0;
                norms[6] = 0;
                norms[7] = 1;
                norms[8] = 0;
                norms[9] = 0;
                norms[10] = 1;
                norms[11] = 0;
                break;
            }
            case BOTTOM:
            {
                norms[0] = 0;
                norms[1] = -1;
                norms[2] = 0;
                norms[3] = 0;
                norms[4] = -1;
                norms[5] = 0;
                norms[6] = 0;
                norms[7] = -1;
                norms[8] = 0;
                norms[9] = 0;
                norms[10] = -1;
                norms[11] = 0;
                break;
            }
            default:
            {
                norms[0] = (float) (1/Math.sqrt(3));
                norms[1] = (float) (1/Math.sqrt(3));
                norms[2] = (float) (1/Math.sqrt(3));
                norms[3] = (float) (1/Math.sqrt(3));
                norms[4] = (float) (1/Math.sqrt(3));
                norms[5] = (float) (1/Math.sqrt(3));
                norms[6] = (float) (1/Math.sqrt(3));
                norms[7] = (float) (1/Math.sqrt(3));
                norms[8] = (float) (1/Math.sqrt(3));
                norms[9] = (float) (1/Math.sqrt(3));
                norms[10] = (float) (1/Math.sqrt(3));
                norms[11] = (float) (1/Math.sqrt(3));
                break;
            }
        }
        return norms;
    }

    /**
     * does all 4 faces
     * @return
     */
    private static float[] getCrossFaceNormals()
    {
        return crossNormals;
    }

    private static float normXZ = 0.5f;//(float)Math.sqrt(3f);//0.5f;
    private static float normY = 0;//normXZ;//0;
    private static float[] crossNormals =
    {
        //face 1,0;
        -normXZ,normY, normXZ,
        -normXZ,normY, normXZ,
        -normXZ,normY, normXZ,
        -normXZ,normY, normXZ,

            //faces 0,1;
        normXZ,normY,-normXZ,
        normXZ,normY,-normXZ,
        normXZ,normY,-normXZ,
        normXZ,normY,-normXZ,

        //faces 0,0;
        -normXZ,normY,-normXZ,
        -normXZ,normY,-normXZ,
        -normXZ,normY,-normXZ,
        -normXZ,normY,-normXZ,

            //faces 1,1;
        normXZ,normY, normXZ,
        normXZ,normY, normXZ,
        normXZ,normY, normXZ,
        normXZ,normY, normXZ

    };
    /*private static float[] getCrossCubeFaceNormals()
    {
        return crossCubeNormals;
    }
    private static float normXZC = 0.5f;
    private static float[] crossCubeNormals =
            {
                    //face 1,0;
                    -normXZC,0, normXZC,
                    -normXZC,0, normXZC,
                    -normXZC,0, normXZC,
                    -normXZC,0, normXZC,

                    //faces 0,1;
                    normXZC,0,-normXZC,
                    normXZC,0,-normXZC,
                    normXZC,0,-normXZC,
                    normXZC,0,-normXZC,

                    //faces 0,0;
                    -normXZC,0,-normXZC,
                    -normXZC,0,-normXZC,
                    -normXZC,0,-normXZC,
                    -normXZC,0,-normXZC,

                    //faces 1,1;
                    normXZC,0, normXZC,
                    normXZC,0, normXZC,
                    normXZC,0, normXZC,
                    normXZC,0, normXZC

            };*/




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

    private void createCross(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms, Tile thisTile, int x, int y, int z, int metadata)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + WorldOld.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ);

        float[] uvFace;
        float[] normFace;

        for (int j = 0; j < 4; j++)
        {
            verts = getFaceVerticesCross(EnumTileFace.get(j), vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));
            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.PARTICLE, thisTile.getIndex(), metadata);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        normFace = getCrossFaceNormals();
        for (int i = 0; i < normFace.length; i++)
        {
            norms.add(normFace[i]);
        }

    }

    private void createExtendedCross(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms, Tile thisTile, int x, int y, int z, int metadata)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + WorldOld.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ);

        float[] uvFace;
        float[] normFace;

        for (int j = 0; j < 4; j++)
        {
            verts = getFaceVerticesExtendedCross(EnumTileFace.get(j), vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));
            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.PARTICLE, thisTile.getIndex(), metadata);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        normFace = getCrossFaceNormals();
        for (int i = 0; i < normFace.length; i++)
        {
            norms.add(normFace[i]);
        }

    }

    /*private void createExtendedCrossForCube(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms, Tile thisTile, int x, int y, int y)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + WorldOld.CHUNK_OFFSET_Y, y * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ);

        float[] uvFace;
        float[] normFace;

        for (int j = 0; j < 4; j++)
        {
            verts = getFaceVerticesExtendedCross(EnumTileFace.get(j), vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));
            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.PARTICLE, thisTile.getIndex());
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }
        }
        normFace = getCrossCubeFaceNormals();
        for (int i = 0; i < normFace.length; i++)
        {
            norms.add(normFace[i]);
        }

    }*/

    private void createFullCross(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms, Tile thisTile, int x, int y, int z, int metadata)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + WorldOld.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ);

        float[] uvFace;
        float[] normFace;

        for (int j = 0; j < 4; j++)
        {
            verts = getFaceVerticesFullCross(EnumTileFace.get(j), vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));
            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.PARTICLE, thisTile.getIndex(), metadata);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

        }
        normFace = getCrossFaceNormals();
        for (int i = 0; i < normFace.length; i++)
        {
            norms.add(normFace[i]);
        }

    }

    private void createTallFullCross(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms, Tile thisTile, int x, int y, int z, int metadata)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + WorldOld.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ);

        float[] uvFace;
        float[] normFace;

        for (int j = 0; j < 4; j++)
        {
            verts = getFaceVerticesTallFullCross(EnumTileFace.get(j), vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));
            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.PARTICLE, thisTile.getIndex(), metadata);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

        }
        normFace = getCrossFaceNormals();
        for (int i = 0; i < normFace.length; i++)
        {
            norms.add(normFace[i]);
        }

    }

    private void createTallCross(List<Float> vertices, List<Integer> indices, List<Float> uvs, List<Float> norms, Tile thisTile, int x, int y, int z, int metadata)
    {
        float[] verts;

        Vector3f vec = new Vector3f(x * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ, y * Tile.TILE_HEIGHT + WorldOld.CHUNK_OFFSET_Y, z * Tile.TILE_SIZE + WorldOld.CHUNK_OFFSET_XZ);

        float[] uvFace;
        float[] normFace;

        for (int j = 0; j < 4; j++)
        {
            verts = getFaceVerticesTallCross(EnumTileFace.get(j), vec);
            int count = vertices.size() / 3;
            vertices.addAll(ArrayUtils.floatToFloat(verts));
            for (int i = 0; i < indexList.length; i++)
            {
                indices.add(indexList[i] + count);
            }
            uvFace = getFaceUVs(false, EnumTileFace.PARTICLE, thisTile.getIndex(), metadata);
            for (int i = 0; i < uvFace.length; i++)
            {
                uvs.add(uvFace[i]);
            }

        }
        normFace = getCrossFaceNormals();
        for (int i = 0; i < normFace.length; i++)
        {
            norms.add(normFace[i]);
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

    private float[] getFaceUVs(boolean isY, EnumTileFace face, int id, int metadata)
    {
        //System.out.println(id + " " + face);
        int row = Tile.Tiles.get(id).getTextureOffset(face.ordinal(), metadata) % TERRAIN_TEXTURE_ROWS;
        int col = Tile.Tiles.get(id).getTextureOffset(face.ordinal(), metadata) / TERRAIN_TEXTURE_COLS;
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

    private static float[] getFaceVerticesTallCross(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];

        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexCrossList[faceVerticesCross[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexCrossList[faceVerticesCross[face.ordinal()][i]][1]*2 + vec.y;
            fv[i*3+2] = vertexCrossList[faceVerticesCross[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }

    private static float[] getFaceVerticesExtendedCross(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];

        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexExtendedCrossList[faceVerticesCross[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexExtendedCrossList[faceVerticesCross[face.ordinal()][i]][1] + vec.y;
            fv[i*3+2] = vertexExtendedCrossList[faceVerticesCross[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }

    private static float[] getFaceVerticesTallFullCross(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];

        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexFullCrossList[faceVerticesCross[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexFullCrossList[faceVerticesCross[face.ordinal()][i]][1]*2 + vec.y;
            fv[i*3+2] = vertexFullCrossList[faceVerticesCross[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }

    private static float[] getFaceVerticesFullCross(EnumTileFace face, Vector3f vec)
    {
        float[] fv = new float[4*3];

        for (int i = 0; i < 4; i++)
        {
            fv[i*3] = vertexFullCrossList[faceVerticesCross[face.ordinal()][i]][0] + vec.x;
            fv[i*3+1] = vertexFullCrossList[faceVerticesCross[face.ordinal()][i]][1] + vec.y;
            fv[i*3+2] = vertexFullCrossList[faceVerticesCross[face.ordinal()][i]][2] + vec.z;
        }
        return fv;
    }

    private static float diag0 = ((float)Math.sqrt((Tile.TILE_SIZE*Tile.TILE_SIZE)+(Tile.TILE_SIZE*Tile.TILE_SIZE))-Tile.TILE_SIZE)*0.5f;
    private static float diag1 = Tile.TILE_SIZE - diag0;

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

    private static float[][] vertexFullCrossList =
            {
                    new float[]{ 0,0,0}, //0
                    new float[]{ Tile.TILE_SIZE,0,0}, //1
                    new float[]{ 0,0,Tile.TILE_SIZE}, //2
                    new float[]{ Tile.TILE_SIZE,0,Tile.TILE_SIZE}, //3

                    new float[]{ 0,Tile.TILE_HEIGHT,0}, //4
                    new float[]{ Tile.TILE_SIZE,Tile.TILE_HEIGHT,0}, //5
                    new float[]{ 0,Tile.TILE_HEIGHT,Tile.TILE_SIZE}, //6
                    new float[]{ Tile.TILE_SIZE,Tile.TILE_HEIGHT,Tile.TILE_SIZE}  //7
            };

    private static float[][] vertexExtendedCrossList =
            {
                    new float[]{ -diag0,-diag0,-diag0}, //0
                    new float[]{ Tile.TILE_SIZE+diag0,-diag0,-diag0}, //1
                    new float[]{ -diag0,-diag0,Tile.TILE_SIZE+diag0}, //2
                    new float[]{ Tile.TILE_SIZE+diag0,-diag0,Tile.TILE_SIZE+diag0}, //3

                    new float[]{ -diag0,Tile.TILE_HEIGHT+diag0,-diag0}, //4
                    new float[]{ Tile.TILE_SIZE+diag0,Tile.TILE_HEIGHT+diag0,-diag0}, //5
                    new float[]{ -diag0,Tile.TILE_HEIGHT+diag0,Tile.TILE_SIZE+diag0}, //6
                    new float[]{ Tile.TILE_SIZE+diag0,Tile.TILE_HEIGHT+diag0,Tile.TILE_SIZE+diag0}  //7
            };

    private static int[][] faceVerticesCross = //fixed to rotate counterclockwise such that face culling works correctly
            {
                    new int[] { 4,0,3,7 },//faces 1,0
                    new int[] { 7,3,0,4 },//faces 0,1
                    new int[] { 5,1,2,6 },//faces 0,0
                    new int[] { 6,2,1,5 },//faces 1,1
            };

    public static final int TERRAIN_TEXTURE_ROWS = 16;
    public static final int TERRAIN_TEXTURE_COLS = 16;

    private float tileTextureWidth = 1f / TERRAIN_TEXTURE_ROWS;
    private float tileTextureHeight = 1f / TERRAIN_TEXTURE_COLS;

    public void reloadMesh()
    {
        List<Float> vertices = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();
        List<Float> uvs = new ArrayList<Float>();
        List<Float> norms = new ArrayList<Float>();

        createMesh(vertices, indices, uvs, norms);

        rawMesh = loader.reloadToVAO(rawMesh, ArrayUtils.floatFromFloat(vertices),
                ArrayUtils.intFromInteger(indices), ArrayUtils.floatFromFloat(uvs), ArrayUtils.floatFromFloat(norms));
    }
}
