package com.deadvikingstudios.bygul.model.tiles;

import com.deadvikingstudios.bygul.model.world.World;
import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.view.meshes.TileMesh;
import com.deadvikingstudios.bygul.view.meshes.components.TileMeshDef;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;

public class TileLog extends Tile
{
    public TileLog(String unlocalizedName)
    {
        super(unlocalizedName, EnumMaterial.WOOD);
        this.isOpaque = false;
    }

    private static float edge1 = (float) Math.sqrt((0.25f*0.25f) + (0.25f*0.25f));
    private static float edge2 = (float) (Math.sqrt(0.5) - (0.25*Math.cos(Math.toRadians(45))));

    private static final HashMap<Integer, TileMesh> MESHES;

    static
    {
        MESHES = new HashMap<>();
        int l = 6;
        for (int i = 0; i < Math.pow(2, l); i++)
        {
            StringBuilder bin = new StringBuilder(Integer.toBinaryString(i));
            //pads the left side with 0s
            while (bin.length() < l)
                bin.insert(0, "0");
            //converts to a character array for quick access
            char[] chars = bin.toString().toCharArray();
            boolean[] bools = new boolean[l];
            //populates boolean array
            for (int j = 0; j < chars.length; j++)
            {
                bools[j] = chars[j] == '0';
            }
            //populates map
            int n = 0, m = bools.length;
            for (int j = 0; j < m; ++j)
            {
                n = (n << 1) + (bools[j] ? 1 : 0);
            }

            MESHES.put(n, new TileMesh(
                    //Top & Bottom Face
                    new TileMeshDef.Cuboid(new Vector3f(0, -0.0001f, 0), new Vector3f(1, 1.0001f, 1),
                            new TileMeshDef.Quad[]{null, null, null, null, !bools[4] ? new TileMeshDef.Quad(7, EnumTileFace.TOP) : null, !bools[5] ? new TileMeshDef.Quad(7, EnumTileFace.BOTTOM) : null}),
                    //Sides
                    new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1, 1), new TileMeshDef.Quad[] {
                            !bools[0] ? new TileMeshDef.Quad(6, EnumTileFace.NORTH): null, null, !bools[2] ? new TileMeshDef.Quad(6, EnumTileFace.SOUTH) : null, null,
                            null, null}),
                    new TileMeshDef.Cuboid(new Vector3f(0, 0, 0.25f), new Vector3f(1, 1, 0.75f),
                            new TileMeshDef.Quad[]{null, new TileMeshDef.Quad(6, EnumTileFace.EAST), null, new TileMeshDef.Quad(6, EnumTileFace.WEST),
                                    null, null}),
                    //Corners
                    new TileMeshDef.Cuboid(new Vector3f(0.5f - edge2, 0, 0.5f - edge1 / 2), new Vector3f(0.5f + edge2, 1, 0.5f + edge1 / 2), new Vector3f(0, 45, 0),
                            new TileMeshDef.Quad[]{null, new TileMeshDef.Quad(6, EnumTileFace.NULL), null, new TileMeshDef.Quad(6, EnumTileFace.NULL),
                                    null, null}),
                    new TileMeshDef.Cuboid(new Vector3f(0.5f - edge1 / 2, 0, 0.5f - edge2), new Vector3f(0.5f + edge1 / 2, 1, 0.5f + edge2), new Vector3f(0, 45, 0),
                            new TileMeshDef.Quad(6, EnumTileFace.NULL), null, new TileMeshDef.Quad(6, EnumTileFace.NULL), null, null, null),
                    //Side Edges
                    new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1, 1 / 32f),
                            new TileMeshDef.Quad[]{null, null, null, null, !bools[4] ? new TileMeshDef.Quad(6, new float[]{0, 0, 0, 1 / 32f, 1f, 1 / 32f, 1f, 0}, EnumTileFace.TOP) : null,
                                    !bools[5] ?  new TileMeshDef.Quad(6, new float[]{0, 0, 0, 1 / 32f, 1f, 1 / 32f, 1f, 0}, EnumTileFace.BOTTOM) : null}),
                    new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1, 1 / 32f), new Vector3f(0, 90, 0),
                            new TileMeshDef.Quad[]{null, null, null, null, !bools[4] ?  new TileMeshDef.Quad(6, new float[]{0, 0, 0, 1 / 32f, 1f, 1 / 32f, 1f, 0}, EnumTileFace.TOP): null,
                                    !bools[5] ? new TileMeshDef.Quad(6, new float[]{0, 0, 0, 1 / 32f, 1f, 1 / 32f, 1f, 0}, EnumTileFace.BOTTOM) : null}),
                    new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1, 1 / 32f), new Vector3f(0, 180, 0),
                            new TileMeshDef.Quad[]{null, null, null, null, !bools[4] ? new TileMeshDef.Quad(6, new float[]{0, 0, 0, 1 / 32f, 1f, 1 / 32f, 1f, 0}, EnumTileFace.TOP) : null,
                                    !bools[5] ? new TileMeshDef.Quad(6, new float[]{0, 0, 0, 1 / 32f, 1f, 1 / 32f, 1f, 0}, EnumTileFace.BOTTOM) : null}),
                    new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1, 1 / 32f), new Vector3f(0, 270, 0),
                            new TileMeshDef.Quad[]{null, null, null, null, !bools[4] ? new TileMeshDef.Quad(6, new float[]{0, 0, 0, 1 / 32f, 1f, 1 / 32f, 1f, 0}, EnumTileFace.TOP) : null,
                                    !bools[5] ? new TileMeshDef.Quad(6, new float[]{0, 0, 0, 1 / 32f, 1f, 1 / 32f, 1f, 0}, EnumTileFace.BOTTOM) : null}),
                    //Corner Edges
                    new TileMeshDef.Cuboid(new Vector3f(0.5f - edge2, -0.00005f, 0.5f - edge1 / 2), new Vector3f(1 / 32f, 1.00005f, 0.5f + edge1 / 2), new Vector3f(0, 45, 0),
                            new TileMeshDef.Quad[]{null, null, null, null, new TileMeshDef.Quad(6, new float[]{0, 0, 0, 2 / 32f, 1f, 2 / 32f, 1f, 0}, 1, EnumTileFace.TOP),
                                    new TileMeshDef.Quad(6, new float[]{0, 0, 0, 2 / 32f, 1f, 2 / 32f, 1f, 0}, 1, EnumTileFace.BOTTOM)}),
                    new TileMeshDef.Cuboid(new Vector3f(0.5f - edge2, -0.00005f, 0.5f - edge1 / 2), new Vector3f(1 / 32f, 1.00005f, 0.5f + edge1 / 2), new Vector3f(0, 135, 0),
                            new TileMeshDef.Quad[]{null, null, null, null, new TileMeshDef.Quad(6, new float[]{0, 0, 0, 2 / 32f, 1f, 2 / 32f, 1f, 0}, 1, EnumTileFace.TOP),
                                    new TileMeshDef.Quad(6, new float[]{0, 0, 0, 2 / 32f, 1f, 2 / 32f, 1f, 0}, 1, EnumTileFace.BOTTOM)}),
                    new TileMeshDef.Cuboid(new Vector3f(0.5f - edge2, -0.00005f, 0.5f - edge1 / 2), new Vector3f(1 / 32f, 1.00005f, 0.5f + edge1 / 2), new Vector3f(0, 225, 0),
                            new TileMeshDef.Quad[]{null, null, null, null, new TileMeshDef.Quad(6, new float[]{0, 0, 0, 2 / 32f, 1f, 2 / 32f, 1f, 0}, 1, EnumTileFace.TOP),
                                    new TileMeshDef.Quad(6, new float[]{0, 0, 0, 2 / 32f, 1f, 2 / 32f, 1f, 0}, 1, EnumTileFace.BOTTOM)}),
                    new TileMeshDef.Cuboid(new Vector3f(0.5f - edge2, -0.00005f, 0.5f - edge1 / 2), new Vector3f(1 / 32f, 1.00005f, 0.5f + edge1 / 2), new Vector3f(0, 315, 0),
                            new TileMeshDef.Quad[]{null, null, null, null, new TileMeshDef.Quad(6, new float[]{0, 0, 0, 2 / 32f, 1f, 2 / 32f, 1f, 0}, 1, EnumTileFace.TOP),
                                    new TileMeshDef.Quad(6, new float[]{0, 0, 0, 2 / 32f, 1f, 2 / 32f, 1f, 0}, 1, EnumTileFace.BOTTOM)})
            ));
        }

    }

    @Override
    public TileMesh getTileMesh(int metadata, int x, int y, int z)
    {
        boolean[] bools = new boolean[6];
        for (int i = 0; i < 6; i++)
        {
            bools[i] = World.getCurrentWorld().getCurrentBuildStructure().getTile(EnumTileFace.get(i).getOffset(x, y, z)) == this;
        }
        int n = 0, m = bools.length;
        for (int j = 0; j < m; ++j)
        {
            n = (n << 1) + (bools[j] ? 1 : 0);
        }

        return MESHES.get(n);
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {
//        //int metadata = world.getMetadata(x,y,y);
//        Tile tileUp = NorseTiles.get(world.getTile(x,y+1,y));
//        Tile tileDown = NorseTiles.get(world.getTile(x,y-1,y));
//        int metaData = world.getMetadata(x,y,y);
//        int tileDownMeta = world.getMetadata(x,y-1,y);
//
//        if(tileUp == this)
//        {
//            if(tileDown == NorseTiles.tileSoil || tileDown == NorseTiles.tileGrass || (tileDown == this && tileDownMeta > metaData))
//            {
//                //System.out.println("tileDown is not a Tree");
//                world.incrementMetadata(x,y,y);
//            }
//        }
//        else //if(metaData > growthMeta)
//        {
//            world.setTile(NorseTiles.tileTrunkFir, x, y+1, y, true);
//
//            world.setTile(NorseTiles.tileLeaves, x, y+2, y, true);
//            world.setTile(NorseTiles.tileLeaves, x, y+3, y, true);
//
//
//            WorldOld.getWorld().setTile(Tile.NorseTiles.tileLeaves, x + 1, y + 1, y, true);
//            WorldOld.getWorld().setTile(Tile.NorseTiles.tileLeaves, x - 1, y + 1, y, true);
//            WorldOld.getWorld().setTile(Tile.NorseTiles.tileLeaves, x, y + 1, y + 1, true);
//            WorldOld.getWorld().setTile(Tile.NorseTiles.tileLeaves, x, y + 1, y - 1, true);
//
//            if (y % 2 == 0)
//            {
//                WorldOld.getWorld().setTile(Tile.NorseTiles.tileLeaves, x + 1, y + 1, y - 1, true);
//                WorldOld.getWorld().setTile(Tile.NorseTiles.tileLeaves, x - 1, y + 1, y + 1, true);
//                WorldOld.getWorld().setTile(Tile.NorseTiles.tileLeaves, x + 1, y + 1, y + 1, true);
//                WorldOld.getWorld().setTile(Tile.NorseTiles.tileLeaves, x - 1, y + 1, y - 1, true);
//            }
//        }
        //System.out.println(metaData);
        /*if(this == NorseTiles.tileLogThin && tileDown != NorseTiles.tileLogThin && metaData >= growthMeta * 4)
        {
            world.setTile(NorseTiles.tileLogMed, x, y, y);
        }
        else if(this ==NorseTiles.tileLogMed && tileDown != NorseTiles.tileLogThin && tileDown != NorseTiles.tileLogMed  && metaData >= growthMeta*2)
        {
            world.setTile(NorseTiles.tileLogThick, x, y, y);
        }
        else if(this == NorseTiles.tileLogThick&& tileDown != NorseTiles.tileLogThin && tileDown != NorseTiles.tileLogMed && tileDown != NorseTiles.tileLogThick  && metaData >= growthMeta)
        {
            world.setTile(NorseTiles.tileLogFull, x, y, y);
        }*/
    }
}
