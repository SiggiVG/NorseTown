package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.view.meshes.TileMesh;
import com.deadvikingstudios.norsetown.view.meshes.components.TileMeshDef;

public class TileTree extends Tile
{
    public TileTree(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.WOOD);
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

    private static int growthMeta = 3;

    private static final TileMesh MESH = new TileMesh(new TileMeshDef.Cuboid(
            new TileMeshDef.Quad(6, EnumTileFace.NORTH),
            new TileMeshDef.Quad(6, EnumTileFace.EAST),
            new TileMeshDef.Quad(6, EnumTileFace.SOUTH),
            new TileMeshDef.Quad(6, EnumTileFace.WEST),
            new TileMeshDef.Quad(7, EnumTileFace.TOP),
            new TileMeshDef.Quad(7, EnumTileFace.BOTTOM)
    ));
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
