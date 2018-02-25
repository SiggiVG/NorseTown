package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;
import com.deadvikingstudios.norsetown.view.meshes.components.TileMeshDef;
import org.lwjgl.util.vector.Vector3f;

public class TileLog extends Tile
{
    public TileLog(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.WOOD);
        this.isOpaque = false;
    }

    @Override
    @Deprecated
    public EnumTileShape getTileShape(int metadata)
    {
//        if(metadata > growthMeta*3)
        {
            return EnumTileShape.FULL_CUBE;
        }
//        else if(metadata > growthMeta*2)
//        {
//            return EnumTileShape.COL_THICK;
//        }
//        else if(metadata > growthMeta)
//        {
//            return EnumTileShape.COL_MED;
//        }
//        else
//        {
//            return EnumTileShape.COL_THIN;
//        }
    }


    private static float edge1 = (float) Math.sqrt((0.25f*0.25f) + (0.25f*0.25f));
    private static float edge2 = (float) (Math.sqrt(0.5) - (0.25*Math.cos(Math.toRadians(45))));

    private static final TileMesh MESH = new TileMesh(
            //Top & Bottom Face
            new TileMeshDef.Cuboid( new Vector3f(0,-0.0001f,0), new Vector3f(1,1.0001f,1),
                    new TileMeshDef.Quad[]{null,null,null,null, new TileMeshDef.Quad(7, EnumTileFace.TOP),new TileMeshDef.Quad(7, EnumTileFace.BOTTOM)}),
            //Sides
            new TileMeshDef.Cuboid( new Vector3f(0.25f, 0, 0), new Vector3f(0.75f,1,1),
                    new TileMeshDef.Quad(6, EnumTileFace.NORTH), null, new TileMeshDef.Quad(6, EnumTileFace.SOUTH), null,
                    null, null),
            new TileMeshDef.Cuboid( new Vector3f(0, 0, 0.25f), new Vector3f(1,1,0.75f),
                new TileMeshDef.Quad[] {null, new TileMeshDef.Quad(6, EnumTileFace.EAST), null, new TileMeshDef.Quad(6, EnumTileFace.WEST),
                        null, null}),
            //Corners
            new TileMeshDef.Cuboid( new Vector3f(0.5f-edge2, 0, 0.5f - edge1/2), new Vector3f(0.5f+edge2,1,0.5f + edge1/2), new Vector3f(0,45,0),
                new TileMeshDef.Quad[] {null, new TileMeshDef.Quad(6, EnumTileFace.NULL), null, new TileMeshDef.Quad(6, EnumTileFace.NULL),
                        null, null}),
            new TileMeshDef.Cuboid( new Vector3f(0.5f - edge1/2, 0, 0.5f-edge2), new Vector3f(0.5f + edge1/2 ,1,0.5f+edge2), new Vector3f(0,45,0),
                    new TileMeshDef.Quad(6, EnumTileFace.NULL), null, new TileMeshDef.Quad(6, EnumTileFace.NULL), null, null, null),
            //Side Edges
            new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1,1/32f),
                new TileMeshDef.Quad[]{null,null,null,null,new TileMeshDef.Quad(6, new float[]{ 0,0, 0,1/32f, 1f,1/32f, 1f,0 }, EnumTileFace.TOP),
                    new TileMeshDef.Quad(6, new float[]{ 0,0, 0,1/32f, 1f,1/32f, 1f,0 }, EnumTileFace.BOTTOM)}),
            new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1,1/32f), new Vector3f(0,90,0),
                    new TileMeshDef.Quad[]{null,null,null,null,new TileMeshDef.Quad(6, new float[]{ 0,0, 0,1/32f, 1f,1/32f, 1f,0 }, EnumTileFace.TOP),
                            new TileMeshDef.Quad(6, new float[]{ 0,0, 0,1/32f, 1f,1/32f, 1f,0 }, EnumTileFace.BOTTOM)}),
            new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1,1/32f), new Vector3f(0,180,0),
                    new TileMeshDef.Quad[]{null,null,null,null,new TileMeshDef.Quad(6, new float[]{ 0,0, 0,1/32f, 1f,1/32f, 1f,0 }, EnumTileFace.TOP),
                            new TileMeshDef.Quad(6, new float[]{ 0,0, 0,1/32f, 1f,1/32f, 1f,0 }, EnumTileFace.BOTTOM)}),
            new TileMeshDef.Cuboid(new Vector3f(0.25f, 0, 0), new Vector3f(0.75f, 1,1/32f), new Vector3f(0,270,0),
                    new TileMeshDef.Quad[]{null,null,null,null,new TileMeshDef.Quad(6, new float[]{ 0,0, 0,1/32f, 1f,1/32f, 1f,0 }, EnumTileFace.TOP),
                            new TileMeshDef.Quad(6, new float[]{ 0,0, 0,1/32f, 1f,1/32f, 1f,0 }, EnumTileFace.BOTTOM)}),
            //Corner Edges
            new TileMeshDef.Cuboid( new Vector3f(0.5f-edge2, -0.00005f, 0.5f - edge1/2), new Vector3f(1/32f,1.00005f,0.5f + edge1/2), new Vector3f(0,45,0),
                    new TileMeshDef.Quad[] {null,null,null,null,new TileMeshDef.Quad(6, new float[]{ 0,0, 0,2/32f, 1f,2/32f, 1f,0 }, 1, EnumTileFace.TOP),
                            new TileMeshDef.Quad(6, new float[]{ 0,0, 0,2/32f, 1f,2/32f, 1f,0 }, 1,EnumTileFace.BOTTOM)}),
            new TileMeshDef.Cuboid( new Vector3f(0.5f-edge2, -0.00005f, 0.5f - edge1/2), new Vector3f(1/32f,1.00005f,0.5f + edge1/2), new Vector3f(0,135,0),
                    new TileMeshDef.Quad[] {null,null,null,null,new TileMeshDef.Quad(6, new float[]{ 0,0, 0,2/32f, 1f,2/32f, 1f,0 }, 1, EnumTileFace.TOP),
                            new TileMeshDef.Quad(6, new float[]{ 0,0, 0,2/32f, 1f,2/32f, 1f,0 }, 1,EnumTileFace.BOTTOM)}),
            new TileMeshDef.Cuboid( new Vector3f(0.5f-edge2, -0.00005f, 0.5f - edge1/2), new Vector3f(1/32f,1.00005f,0.5f + edge1/2), new Vector3f(0,225,0),
                    new TileMeshDef.Quad[] {null,null,null,null,new TileMeshDef.Quad(6, new float[]{ 0,0, 0,2/32f, 1f,2/32f, 1f,0 }, 1, EnumTileFace.TOP),
                            new TileMeshDef.Quad(6, new float[]{ 0,0, 0,2/32f, 1f,2/32f, 1f,0 }, 1,EnumTileFace.BOTTOM)}),
            new TileMeshDef.Cuboid( new Vector3f(0.5f-edge2, -0.00005f, 0.5f - edge1/2), new Vector3f(1/32f,1.00005f,0.5f + edge1/2), new Vector3f(0,315,0),
                    new TileMeshDef.Quad[] {null,null,null,null,new TileMeshDef.Quad(6, new float[]{ 0,0, 0,2/32f, 1f,2/32f, 1f,0 }, 1, EnumTileFace.TOP),
                            new TileMeshDef.Quad(6, new float[]{ 0,0, 0,2/32f, 1f,2/32f, 1f,0 }, 1,EnumTileFace.BOTTOM)})
    );
    @Override
    public TileMesh getTileMesh(int metadata)
    {
        return MESH;
    }

    @Override
    public void update(Structure structure, int x, int y, int z)
    {
//        //int metadata = world.getMetadata(x,y,y);
//        Tile tileUp = Tiles.get(world.getTile(x,y+1,y));
//        Tile tileDown = Tiles.get(world.getTile(x,y-1,y));
//        int metaData = world.getMetadata(x,y,y);
//        int tileDownMeta = world.getMetadata(x,y-1,y);
//
//        if(tileUp == this)
//        {
//            if(tileDown == Tiles.tileSoil || tileDown == Tiles.tileGrass || (tileDown == this && tileDownMeta > metaData))
//            {
//                //System.out.println("tileDown is not a Tree");
//                world.incrementMetadata(x,y,y);
//            }
//        }
//        else //if(metaData > growthMeta)
//        {
//            world.setTile(Tiles.tileTrunkFir, x, y+1, y, true);
//
//            world.setTile(Tiles.tileLeaves, x, y+2, y, true);
//            world.setTile(Tiles.tileLeaves, x, y+3, y, true);
//
//
//            WorldOld.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, y, true);
//            WorldOld.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, y, true);
//            WorldOld.getWorld().setTile(Tile.Tiles.tileLeaves, x, y + 1, y + 1, true);
//            WorldOld.getWorld().setTile(Tile.Tiles.tileLeaves, x, y + 1, y - 1, true);
//
//            if (y % 2 == 0)
//            {
//                WorldOld.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, y - 1, true);
//                WorldOld.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, y + 1, true);
//                WorldOld.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, y + 1, true);
//                WorldOld.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, y - 1, true);
//            }
//        }
        //System.out.println(metaData);
        /*if(this == Tiles.tileLogThin && tileDown != Tiles.tileLogThin && metaData >= growthMeta * 4)
        {
            world.setTile(Tiles.tileLogMed, x, y, y);
        }
        else if(this ==Tiles.tileLogMed && tileDown != Tiles.tileLogThin && tileDown != Tiles.tileLogMed  && metaData >= growthMeta*2)
        {
            world.setTile(Tiles.tileLogThick, x, y, y);
        }
        else if(this == Tiles.tileLogThick&& tileDown != Tiles.tileLogThin && tileDown != Tiles.tileLogMed && tileDown != Tiles.tileLogThick  && metaData >= growthMeta)
        {
            world.setTile(Tiles.tileLogFull, x, y, y);
        }*/
    }
}
