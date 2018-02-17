package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

public class StructureTree extends Structure
{
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
}
