package com.deadvikingstudios.norsetown.view.meshes.components;

import com.deadvikingstudios.norsetown.model.tiles.EnumTileFace;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;
import com.sun.istack.internal.NotNull;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TileMeshDef
{
    //TODO: move to a TextureAtlas class definition
    private static final int TERRAIN_TEXTURE_ROWS = 16;
    private static final int TERRAIN_TEXTURE_COLS = 16;

    private static final float tileTextureWidth = 1f / TERRAIN_TEXTURE_ROWS;
    private static final float tileTextureHeight = 1f / TERRAIN_TEXTURE_COLS;

    /**
     * @param cuboid    the cuboid to be drawn
     * @param cullFaces a boolean[6] that has values of true whenever a solid block is on that Face {@link EnumTileFace}
     */
    public static List<TileMesh.Face> drawCuboid(@NotNull final Cuboid cuboid, @NotNull final boolean[] cullFaces)
    {
//        assert cuboid.faces.length == 6;
        List<TileMesh.Face> faces = new ArrayList<>();

        for (int i = 0; i < 6; i++)
        {
            Quad quad = cuboid.faces[i];
            if(quad == null) continue;
            //if the quad shouldn't be culled
            if (quad.cullFace == EnumTileFace.NULL)
            {
                faces.add(drawQuad(EnumTileFace.values()[i], quad, cuboid));
            }
            else if(quad.cullFace == EnumTileFace.PARTICLE){} //do nothing
            else if(cullFaces[quad.cullFace.ordinal()])
            {
                faces.add(drawQuad(EnumTileFace.values()[i], quad, cuboid));
            }
        }

        return faces;
    }

    public static class Cuboid
    {
        //dimensions
        Vector3f from = new Vector3f(0, 0, 0);
        Vector3f to = new Vector3f(1, 1, 1);

        /**
         * The point around which the cuboid will be rotated
         */
        Vector3f originOfRotation = new Vector3f(0.5f, 0.5f, 0.5f);
        /**
         * The amount to rotate each axis by, in degrees
         */
        Vector3f rotation = new Vector3f(0, 0, 0);
        /**
         * Contains the Quad faces of the cuboid
         * North  0
         * East   1
         * South  2
         * West   3
         * Top    4
         * Bottom 5
         */
        Quad[] faces = new Quad[6];
        /**
         * should generate a full cube of MISSING_TEXTURE
         */
        public Cuboid()
        {
            this(0);
        }

        /**
         * should generate a full cube of
         * @param texture
         */
        public Cuboid(int texture)
        {
            for (int i = 0; i < faces.length; i++)
            {
                faces[i] = new Quad(texture, EnumTileFace.get(i));
            }
        }

        public Cuboid(Quad ... quads)
        {
            for (int i = 0; i < 6 && i < quads.length; i++)
            {
                this.faces[i] = quads[i];
            }
        }

        public Cuboid(int texture, Vector3f from, Vector3f to, boolean cullFaces)
        {
            this(texture);
            this.to = new Vector3f(to);
            this.from = new Vector3f(from);

            if(!cullFaces)
            {
                for (int i = 0; i < 6; i++)
                {
                    this.faces[i].cullFace = EnumTileFace.NULL;
                }
            }
        }

        public Cuboid(Vector3f from, Vector3f to, Quad ... quads)
        {
            this(quads);
            this.to = new Vector3f(to);
            this.from = new Vector3f(from);
        }

        public Cuboid(int texture, Vector3f from, Vector3f to)
        {
            this(texture, from, to, false);
        }

        public Cuboid(int texture, Vector3f from, Vector3f to, Vector3f rotation)
        {
            this(texture, from, to, false);
            this.rotation = rotation;
        }

        public Cuboid(Vector3f from, Vector3f to, Vector3f rotation, Quad ... quads)
        {
            this(from, to, quads);
            this.rotation = rotation;
        }

        public Cuboid(int texture, Vector3f from, Vector3f to, Vector3f rotation, Vector3f originOfRotation)
        {
            this(texture, from, to, rotation);
            this.originOfRotation = originOfRotation;
        }

        public Cuboid(Vector3f from, Vector3f to, Vector3f rotation, Vector3f originOfRotation, Quad ... quads)
        {
            this(from, to, rotation, quads);
            this.originOfRotation = originOfRotation;
        }
    }

    private static final Vector3f ORIGIN_NORMS = new Vector3f();

    /**
     * Draws a face of the cuboid
     * @param face
     * @param quad
     * @param cuboid
     */
    private static TileMesh.Face drawQuad(@NotNull final EnumTileFace face, @NotNull final Quad quad, @NotNull final Cuboid cuboid)
    {
        if(face == EnumTileFace.NULL || face == EnumTileFace.PARTICLE) return null;

        float[] vertices = new float[4*3];
        int[] indices;
        float[] uvs = new float[4*2];
        float[] norms = new float[4*3];

        //generate vertices
        for (int i = 0; i < 4; i++)
        {
            int ver = faceVertices[face.ordinal()][i];
            Vector3f point = new Vector3f
                    (
                            (vertexCuboidList[ver][0] ? cuboid.to.x : cuboid.from.x), //X
                            (vertexCuboidList[ver][1] ? cuboid.to.y : cuboid.from.y), //Y
                            (vertexCuboidList[ver][2] ? cuboid.to.z : cuboid.from.z)  //Z
                    );
            Vector3f normFace = new Vector3f
                    (
                            normList[face.ordinal()][0],
                            normList[face.ordinal()][1],
                            normList[face.ordinal()][2]
                    );

//            rotate the vertice position
            if(cuboid.rotation.x != 0)
            {
                point = rotateVector(point, cuboid.originOfRotation, cuboid.rotation.x, 0);
                normFace = rotateVector(normFace, ORIGIN_NORMS, cuboid.rotation.x, 0);
            }
            if(cuboid.rotation.y != 0)
            {
                point = rotateVector(point, cuboid.originOfRotation, cuboid.rotation.y, 1);
                normFace = rotateVector(normFace, ORIGIN_NORMS, cuboid.rotation.y, 1);
            }
            if(cuboid.rotation.z != 0)
            {
                point = rotateVector(point, cuboid.originOfRotation, cuboid.rotation.z, 2);
                normFace = rotateVector(normFace, ORIGIN_NORMS, cuboid.rotation.z, 2);
            }

            //offset vertice by position
            vertices[i*3] = point.x;// + position.x;
            vertices[i*3+1] = point.y;// + position.y;
            vertices[i*3+2] = point.z;// + position.z;

            //norms
            normFace = normFace.normalise(normFace);
            norms[i*3] = normFace.x;
            norms[i*3+1] = normFace.y;
            norms[i*3+2] = normFace.z;
        }

        indices = indexList.clone();

        //UV coordinates
        for (int i = 0; i < uvs.length; i++)
        {
            uvs[i] = quad.uv[i];
        }

        return new TileMesh.Face(vertices, indices, uvs, norms);
    }

    public static class Quad
    {
        /**
         * Texture in the atlas that is used
         * should be between 0 and {@link this#TERRAIN_TEXTURE_ROWS}*{@link this#TERRAIN_TEXTURE_COLS}
         */
        int texture = 0;
        /**
         * where on the texture to draw from
         * of the scheme [x1,y1,x2,y2], where each point is 2D
         */
        float[] uv = new float[]
                {
                        0,0,
                        0,tileTextureHeight,
                        tileTextureWidth,tileTextureHeight,
                        tileTextureWidth,0
                };
        /**
         * Defines whether the face should be culled when a solid block is on that side of it.
         */
        EnumTileFace cullFace = EnumTileFace.NULL;
        /**
         * rotation of the texture by 0, 90, 180, or 270 degrees
         * 0-3
         */
        int rotation = 0;

        public Quad()
        {
            this(0);
        }

        public Quad(int texture)
        {
            this(texture, new float[]
                    {
                            0,0,
                            0,tileTextureHeight,
                            tileTextureWidth,tileTextureHeight,
                            tileTextureWidth,0
                    });
        }

        public Quad(int texture, EnumTileFace cullFace)
        {
            this(texture);
            this.cullFace = cullFace;
            if(this.cullFace == null || this.cullFace == EnumTileFace.PARTICLE) this.cullFace = EnumTileFace.NULL;
        }

        public Quad(int texture, @NotNull float[] uv)
        {
            this(texture, uv, 0);
        }

        public Quad(int texture, float[] uv, EnumTileFace cullFace)
        {
            this(texture, uv, 0, cullFace);
        }

        public Quad(int texture, float[] uv, int rotation)
        {
            this(texture, uv, rotation, EnumTileFace.NULL);
        }

        public Quad(int texture, float[] uv, int rotation, EnumTileFace cullFace)
        {
            this.texture = texture;
            this.uv = genUV(rotation, uv);
            this.cullFace = cullFace;
            if(this.cullFace == null || this.cullFace == EnumTileFace.PARTICLE) this.cullFace = EnumTileFace.NULL;
        }

        /**
         * Rotates uv coord
         * @param rotation 0:0, 1:90, 2:180, 3:270
         * @param uvIn the UVs to be rotated
         * @return the generated UVs
         */
        private float[] genUV(int rotation, float[] uvIn)
        {
            assert uvIn.length == 4*2;

            float r0 = (this.texture % TERRAIN_TEXTURE_ROWS) * tileTextureWidth;
            float c0 = (this.texture / TERRAIN_TEXTURE_COLS) * tileTextureHeight;

            float[] uvs = new float[]
                    {
                            r0 + uvIn[0], c0 + uvIn[1],
                            r0 + uvIn[2], c0 + uvIn[3],
                            r0 + uvIn[4], c0 + uvIn[5],
                            r0 + uvIn[6], c0 + uvIn[7]
                    };

            for (int i = 0; i < rotation; i++)
            {
                float x = uvs[0],   y = uvs[1];
                uvs[0] = uvs[2];    uvs[1] = uvs[3];
                uvs[2] = uvs[4];    uvs[3] = uvs[5];
                uvs[6] = x;         uvs[7] = y;
            }
            return uvs;
        }
    }

    private static Vector3f rotateVector(@NotNull Vector3f point, @NotNull Vector3f origin, float degrees, int axis)
    {
        //doesnt have to rotate
        if(degrees == 0) return point;

        if(axis >= 0 && axis < 3)
        { //valid axis
            //translate
            float tPointX = point.x - origin.x;
            float tPointY = point.y - origin.y;
            float tPointZ = point.z - origin.z;

            double radians = Math.toRadians(degrees);

            float temp = 0;
            //rotate
            switch (axis)
            {
                case 0: //x
                    temp = tPointY;
                    //x point - skipped
                    //y point
                    tPointY = (float) ((tPointY * Math.cos(radians)) - (tPointZ * Math.sin(radians)));
                    //z point
                    tPointZ = (float) ((temp * Math.sin(radians)) + (tPointZ * Math.cos(radians)));
                    break;
                case 1: //y
                    temp = tPointX;
                    //x point
                    tPointX = (float) ((tPointX * Math.cos(radians)) + (tPointZ * Math.sin(radians)));
                    //y point - skipped
                    //z point
                    tPointZ = (float) ((-(temp * Math.sin(radians))) + (tPointZ * Math.cos(radians)));
                    break;
                case 2: //z
                    temp = tPointX;
                    //x point
                    tPointX = (float) ((tPointX * Math.cos(radians)) - (tPointY * Math.sin(radians)));
                    //y point
                    tPointY = (float) ((temp * Math.sin(radians)) + (tPointY * Math.cos(radians)));
                    //z point - skipped
                    break;
                default: //should never occur
                    break;
            }

            //translate back
            return new Vector3f(
                    tPointX + origin.x,
                    tPointY + origin.y,
                    tPointZ + origin.z
            );
        }
        //axis is invalid
        return point;
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

    private static boolean[][] vertexCuboidList =
            {
                    new boolean[]{false, false, false}, //0
                    new boolean[]{true,  false, false}, //1
                    new boolean[]{false, false, true }, //2
                    new boolean[]{true,  false, true }, //3

                    new boolean[]{false, true,  false}, //4
                    new boolean[]{true,  true,  false}, //5
                    new boolean[]{false, true,  true }, //6
                    new boolean[]{true,  true,  true }  //7
            };

    private static int[] indexList =
            {
                    0,1,3,
                    3,1,2
            };

    private static float[][] normList =
            {
                    new float[]{0,0,1},
                    new float[]{1,0,0},
                    new float[]{0,0,-1},
                    new float[]{-1,0,0},
                    new float[]{0,1,0},
                    new float[]{0,-1,0},
            };
}
