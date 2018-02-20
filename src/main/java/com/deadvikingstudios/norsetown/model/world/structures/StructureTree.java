package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.model.items.Item;
import com.deadvikingstudios.norsetown.model.items.ItemStack;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.utils.vector.Vector2i;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructureTree extends Structure
{
    private Tile woodBlock = Tile.Tiles.tileTrunkFir, leavesBlock = Tile.Tiles.tileLeaves;
    private Item woodItem = Item.Items.itemTimberFir, leavesItem = Item.Items.itemLeavesFir;

    public StructureTree(Vector3i pos)
    {
        super(pos, true);
    }

    @Override
    public void init()
    {
        this.setTile(Tile.Tiles.tileGrass, 0,0,0);
        for (int i = 1; i < 12; i++)
        {
            this.setTile(Tile.Tiles.tileTrunkFir, 0,i,0);
            if(i > 3)
            {
                this.setTile(Tile.Tiles.tileLeaves, 0, i, 1);
                this.setTile(Tile.Tiles.tileLeaves, 0, i, -1);
                this.setTile(Tile.Tiles.tileLeaves, 1, i, 0);
                this.setTile(Tile.Tiles.tileLeaves, -1, i, 0);

                if (i % 2 == 0)
                {
                    this.setTile(Tile.Tiles.tileLeaves, 1, i, 1);
                    this.setTile(Tile.Tiles.tileLeaves, 1, i, -1);
                    this.setTile(Tile.Tiles.tileLeaves, -1, i, 1);
                    this.setTile(Tile.Tiles.tileLeaves, -1, i, -1);
                }
            }
            if(i > 9)
            {
                this.setTile(Tile.Tiles.tileLeaves, 0,10,0);
                this.setTile(Tile.Tiles.tileLeaves, 0,11,0);
                this.setTile(Tile.Tiles.tileLeaves, 0,12,0);
            }
        }
        this.setTile(Tile.Tiles.tileLeaves, 0,9,1);
        this.setTile(Tile.Tiles.tileLeaves, 0,9,-1);
        this.setTile(Tile.Tiles.tileLeaves, 1,9,0);
        this.setTile(Tile.Tiles.tileLeaves, -1,9,0);
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
                            woodAmount += (chunk.getTile(i,j,k).equals(this.woodBlock))? 1 : 0;
                            leavesAmount += (chunk.getTile(i,j,k).equals(this.leavesBlock))? 1 : 0;
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
