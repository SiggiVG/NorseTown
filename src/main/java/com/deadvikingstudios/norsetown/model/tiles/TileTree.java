package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;

public class TileTree extends Tile
{
    public TileTree(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.WOOD);
    }

    @Override
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

    @Override
    public void update(Structure structure, int x, int y, int z)
    {
//        //int metadata = world.getMetadata(x,y,z);
//        Tile tileUp = Tiles.get(world.getTile(x,y+1,z));
//        Tile tileDown = Tiles.get(world.getTile(x,y-1,z));
//        int metaData = world.getMetadata(x,y,z);
//        int tileDownMeta = world.getMetadata(x,y-1,z);
//
//        if(tileUp == this)
//        {
//            if(tileDown == Tiles.tileSoil || tileDown == Tiles.tileGrass || (tileDown == this && tileDownMeta > metaData))
//            {
//                //System.out.println("tileDown is not a Tree");
//                world.incrementMetadata(x,y,z);
//            }
//        }
//        else //if(metaData > growthMeta)
//        {
//            world.setTile(Tiles.tileTrunkFir, x, y+1, z, true);
//
//            world.setTile(Tiles.tileLeaves, x, y+2, z, true);
//            world.setTile(Tiles.tileLeaves, x, y+3, z, true);
//
//
//            World.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, z, true);
//            World.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, z, true);
//            World.getWorld().setTile(Tile.Tiles.tileLeaves, x, y + 1, z + 1, true);
//            World.getWorld().setTile(Tile.Tiles.tileLeaves, x, y + 1, z - 1, true);
//
//            if (y % 2 == 0)
//            {
//                World.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, z - 1, true);
//                World.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, z + 1, true);
//                World.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, z + 1, true);
//                World.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, z - 1, true);
//            }
//        }
        //System.out.println(metaData);
        /*if(this == Tiles.tileLogThin && tileDown != Tiles.tileLogThin && metaData >= growthMeta * 4)
        {
            world.setTile(Tiles.tileLogMed, x, y, z);
        }
        else if(this ==Tiles.tileLogMed && tileDown != Tiles.tileLogThin && tileDown != Tiles.tileLogMed  && metaData >= growthMeta*2)
        {
            world.setTile(Tiles.tileLogThick, x, y, z);
        }
        else if(this == Tiles.tileLogThick&& tileDown != Tiles.tileLogThin && tileDown != Tiles.tileLogMed && tileDown != Tiles.tileLogThick  && metaData >= growthMeta)
        {
            world.setTile(Tiles.tileLogFull, x, y, z);
        }*/
    }
}
