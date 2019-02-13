package com.deadvikingstudios.bygul.model.world.structures;

import com.deadvikingstudios.bygul.model.items.Item;
import com.deadvikingstudios.bygul.model.items.ItemStack;
import com.deadvikingstudios.bygul.model.tiles.Tile;
import com.deadvikingstudios.bygul.model.world.World;
import com.deadvikingstudios.bygul.utils.vector.Vector2i;
import com.deadvikingstudios.bygul.utils.vector.Vector3i;
import com.deadvikingstudios.norsetown.model.tiles.NorseTiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructureTree extends Structure
{
    private Tile woodTile, leavesTile;
    private Item woodItem, leavesItem;

    public StructureTree(Vector3i pos, Structure parent, Tile woodTile, Tile leavesTile)
    {
        super(pos, parent, false);
        this.woodTile = woodTile;
        this.leavesTile = leavesTile;
        init();
    }

    @Override
    public void init()
    {
        this.genTrunk(0,0,0, 13);

//        this.setTile(NorseTiles.tileGrass, 0,0,0);
//        for (int i = 0; i < 12; i++)
//        {
//            this.setTile(NorseTiles.tileTrunkFir, 0, i, 0);
//
//            if(i > 3)
//            {
//                this.setTile(NorseTiles.tileLeaves, 0, i, 1);
//                this.setTile(NorseTiles.tileLeaves, 0, i, -1);
//                this.setTile(NorseTiles.tileLeaves, 1, i, 0);
//                this.setTile(NorseTiles.tileLeaves, -1, i, 0);
//
//                if (i % 2 == 0)
//                {
//                    this.setTile(NorseTiles.tileLeaves, 1, i, 1);
//                    this.setTile(NorseTiles.tileLeaves, 1, i, -1);
//                    this.setTile(NorseTiles.tileLeaves, -1, i, 1);
//                    this.setTile(NorseTiles.tileLeaves, -1, i, -1);
//                }
//            }
//            if(i > 9)
//            {
//                this.setTile(NorseTiles.tileLeaves, 0,10,0);
//                this.setTile(NorseTiles.tileLeaves, 0,11,0);
//                this.setTile(NorseTiles.tileLeaves, 0,12,0);
//            }
//        }
//        this.setTile(NorseTiles.tileLeaves, 0,9,1);
//        this.setTile(NorseTiles.tileLeaves, 0,9,-1);
//        this.setTile(NorseTiles.tileLeaves, 1,9,0);
//        this.setTile(NorseTiles.tileLeaves, -1,9,0);
    }

    private void genRoots(int x, int y, int z)
    {

    }

    private void genTrunk(int x, int y, int z, int length)
    {
        for (int i = y; i < y+length; i++)
        {
            this.setTile(this.woodTile, x, i, z);
        }

        for (int i = y+length; i > y+2; i-=4)
        {
            this.genLeaves(x,i,z, World.getUpdateRandom().nextInt(3) +1 );
        }

    }

    private void genBranches(int x, int y, int z)
    {

    }

    private void genLeaves(int x, int y, int z, int length)
    {
        if(this.getTile(x,y,x) == Tile.Tiles.tileAir)
        {
            this.setTile(this.leavesTile, x,y,z);
        }
        if(this.getTile(x,y+1,x) == Tile.Tiles.tileAir)
        {
            this.setTile(this.leavesTile, x,y+1,z);
        }
        if(length >= 1)
        {
            this.setTile(this.leavesTile, x,y,z+1);
            this.setTile(this.leavesTile, x+1,y,z);
            this.setTile(this.leavesTile, x,y,z-1);
            this.setTile(this.leavesTile, x-1,y,z);
        }
        if(length >= 2)
        {
            for (int i = -1; i < 2; i++)
            {

                this.setTile(this.leavesTile, x-1,y-1, z+i);
                if(i != 0) this.setTile(this.leavesTile, x,y-1, z+i);
                this.setTile(this.leavesTile, x+1,y-1, z+i);
            }
//            this.setTile(this.leavesTile, x,y-1,z+1);
//            this.setTile(this.leavesTile, x+1,y-1,z);
//            this.setTile(this.leavesTile, x,y-1,z-1);
//            this.setTile(this.leavesTile, x-1,y-1,z);
//
//            this.setTile(this.leavesTile, x+1,y-1,z+1);
//            this.setTile(this.leavesTile, x+1,y-1,z-1);
//            this.setTile(this.leavesTile, x-1,y-1,z-1);
//            this.setTile(this.leavesTile, x-1,y-1,z+1);
        }

        if(length >= 3)
        {
            this.setTile(this.leavesTile, x,y-2,z+1);
            this.setTile(this.leavesTile, x+1,y-2,z);
            this.setTile(this.leavesTile, x,y-2,z-1);
            this.setTile(this.leavesTile, x-1,y-2,z);

            this.setTile(this.leavesTile, x+1,y-2,z+1);
            this.setTile(this.leavesTile, x+1,y-2,z-1);
            this.setTile(this.leavesTile, x-1,y-2,z-1);
            this.setTile(this.leavesTile, x-1,y-2,z+1);

            for (int i = -1; i < 2; i++)
            {
                this.setTile(this.leavesTile, x-2, y-2, z+i);
                this.setTile(this.leavesTile, x+2, y-2, z+i);
                this.setTile(this.leavesTile, x+i, y-2, z-2);
                this.setTile(this.leavesTile, x+i, y-2, z+2);
            }
        }
    }

    @Override
    public void update()
    {
        super.update();
    }

    /**
     *
     * @return the items dropped by the tree
     */
    @Override
    public List<ItemStack> destroy(Structure parent, boolean dropItems)
    {
        List<ItemStack> ret = null;
        if (dropItems)
        {
            ret = this.getDrops();
            System.out.println("StructureTree destroyed with drops: " + ret.toString());
        }
        else
        {
            System.out.println("StructureTree destroyed");
        }

        super.destroy(parent, dropItems);

        return ret;
    }

    private List<ItemStack> getDrops()
    {
        int woodAmount = 0;
        int leavesAmount = 0;
        for (Map.Entry<Vector2i, ChunkColumn> columnEntry : this.chunks.entrySet())
        {
            for (Map.Entry<Integer, Chunk> chunkEntry : columnEntry.getValue().getChunks().entrySet())
            {
                Chunk chunk = chunkEntry.getValue();

                for (int i = 0; i < Chunk.SIZE; i++)
                {
                    for (int j = 0; j < Chunk.SIZE; j++)
                    {
                        for (int k = 0; k < Chunk.SIZE; k++)
                        {
                            woodAmount += (chunk.getTile(i,j,k).equals(this.woodTile))? 1 : 0;
                            leavesAmount += (chunk.getTile(i,j,k).equals(this.leavesTile))? 1 : 0;
                        }
                    }
                }
            }
        }
        List<ItemStack> ret = new ArrayList<ItemStack>();
        while(woodAmount > 0)
        {
            if(woodAmount >= ItemStack.MAX_AMOUNT)
            {
                ret.add(new ItemStack(this.woodItem, ItemStack.MAX_AMOUNT));
                woodAmount -= ItemStack.MAX_AMOUNT;
            }
            else
            {
                ret.add(new ItemStack(this.woodItem, woodAmount));
                break;
            }
        }
        while(leavesAmount > 0)
        {
            if(leavesAmount >= ItemStack.MAX_AMOUNT)
            {
                ret.add(new ItemStack(this.leavesItem, ItemStack.MAX_AMOUNT));
                leavesAmount -= ItemStack.MAX_AMOUNT;
            }
            else
            {
                ret.add(new ItemStack(this.leavesItem, leavesAmount));
                break;
            }
        }
        return ret;
    }
}
