package com.deadvikingstudios.norsetown.model.tiles;

import com.deadvikingstudios.norsetown.model.world.World;

public class TileTree extends Tile
{
    public TileTree(int index, String unlocalizedName)
    {
        super(index, unlocalizedName, EnumMaterial.WOOD);
    }

    @Override
    public EnumTileShape getTileShape(int metadata)
    {
        if(this.equals(Tiles.tileLogThin))
        {
            return EnumTileShape.COL_THIN;
        }
        if(this.equals(Tiles.tileLogMed))
        {
            return EnumTileShape.COL_MED;
        }
        if(this.equals(Tiles.tileLogThick))
        {
            return EnumTileShape.COL_THICK;
        }
        return EnumTileShape.FULL_CUBE;
    }

    private static int growthMeta = 4;

    @Override
    public void update(World world, int x, int y, int z)
    {
        //int metadata = world.getMetadata(x,y,z);
        Tile tileUp = Tiles.get(world.getTile(x,y+1,z));
        Tile tileDown = Tiles.get(world.getTile(x,y-1,z));
        int metaData = world.getMetadata(x,y,z);

        if(tileUp instanceof TileTree && this != Tiles.tileLogFull)
        {
            if(tileDown == Tiles.tileSoil || tileDown == Tiles.tileGrass)
            {
                //System.out.println("tileDown is not a Tree");
                world.incrementMetadata(x,y,z);
            }
            else
            {
                if(this == Tiles.tileLogThin && metaData < 16)
                {

                    world.incrementMetadata(x,y,z);
                    //world.incrementMetadata(x,y,z);
                    //world.incrementMetadata(x,y-1,z);
                }
                else if(this == Tiles.tileLogMed && tileDown != Tiles.tileLogThin && metaData < 16)
                {
                    world.incrementMetadata(x,y,z);
                    //world.incrementMetadata(x,y-1,z);
                }
                else if(this == Tiles.tileLogThick && tileDown != Tiles.tileLogThin && tileDown != Tiles.tileLogMed  && metaData < 16)
                {
                    world.incrementMetadata(x,y,z);
                    //world.incrementMetadata(x,y-1,z);
                }
            }
        }
        else //if(metaData > growthMeta)
        {
            world.setTile(Tiles.tileLogThin, x, y+1, z, true);

            world.setTile(Tiles.tileLeaves, x, y+2, z, true);
            world.setTile(Tiles.tileLeaves, x, y+3, z, true);

            /*
            World.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, z, true);
            World.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, z, true);
            World.getWorld().setTile(Tile.Tiles.tileLeaves, x, y + 1, z + 1, true);
            World.getWorld().setTile(Tile.Tiles.tileLeaves, x, y + 1, z - 1, true);

            if (y % 2 == 0)
            {
                World.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, z - 1, true);
                World.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, z + 1, true);
                World.getWorld().setTile(Tile.Tiles.tileLeaves, x + 1, y + 1, z + 1, true);
                World.getWorld().setTile(Tile.Tiles.tileLeaves, x - 1, y + 1, z - 1, true);
            }*/
        }
        //System.out.println(metaData);
        if(this == Tiles.tileLogThin && tileDown != Tiles.tileLogThin && metaData >= growthMeta * 4)
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
        }
    }
}
