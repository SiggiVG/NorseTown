package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.Position3i;

import java.util.Map;
import java.util.Random;

public class StructureIsland extends Structure
{
    Random rand;

    @Override
    public void init()
    {
        rand = new Random();
//        for (int i = 0; i < 3; i++)
//        {
//            for (int j = 0; j < 3; j++)
//            {
//                for (int k = 0; k < 3; k++)
//                {
//                    this.setTile(Tile.Tiles.get(rand.nextInt(5)+1), i,j,k, 0);
//                }
//            }
//        }

        //Logger.debug("Structure created with Tile " + this.getTile(0,0,0).getUnlocalizedName());

//
//        for (int i = 0; i < 64; i++)
//        {
//            for (int j = 0; j < 64; j++)
//            {
//                for (int k = 0; k < 64; k++)
//                {
//                    if(rand.nextInt(10) == 0) this.setTile(Tile.Tiles.tileStoneCliff, i, j, k, 0);
//                }
//            }
//        }
    }

    @Override
    public void update()
    {
        for (Map.Entry<Position3i,StructureChunk> entry : this.chunks.entrySet())
        {
            int x = rand.nextInt(StructureChunk.SIZE) + entry.getValue().position.x * StructureChunk.SIZE;
            int y = rand.nextInt(StructureChunk.SIZE) + entry.getValue().position.y * StructureChunk.SIZE;
            int z = rand.nextInt(StructureChunk.SIZE) + entry.getValue().position.z * StructureChunk.SIZE;
            this.getTile(x,y,z).update(this,x,y,z);
        }
        super.update();

    }
}
