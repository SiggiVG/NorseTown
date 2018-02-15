package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.model.world.gen.IslandGenerator;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

import java.util.Random;

public class StructureIsland extends Structure
{
    Random rand;

    IslandGenerator generator;

    public StructureIsland(int i, int j, int k)
    {
        super(new Vector3i(i,j,k),true);
    }

    @Override
    public void init()
    {
        rand = new Random();
        generator = new IslandGenerator(this);
    }

//    int i = 0;

    @Override
    public void update()
    {
//        for (Map.Entry<Vector2i,Chunk> entry : this.chunks.entrySet())
//        {
//            int x = rand.nextInt(StructureChunk.SIZE) + entry.getValue().position.x * StructureChunk.SIZE;
//            int y = rand.nextInt(StructureChunk.SIZE) + entry.getValue().position.y * StructureChunk.SIZE;
//            int y = rand.nextInt(StructureChunk.SIZE) + entry.getValue().position.y * StructureChunk.SIZE;
//            this.getTile(x,y,y).update(this,x,y,y);
//        }
//        super.update();

        /**
         * Test Code, used to test the proper placement and checking of tiles within the
         * [Structure -> ChunkColumn -> Chunk] system.
         */
//        if(i % 60 == 0)
//        {
//            int j = i / 60;
//            this.setTile(Tile.Tiles.tileGrass, j,j,j);
//            this.setTile(Tile.Tiles.tileGrass, -j,-j,-j);
//            this.setTile(Tile.Tiles.tileGrass, 0,0,j);
//            this.setTile(Tile.Tiles.tileGrass, 0,0,-j);
//            this.setTile(Tile.Tiles.tileGrass, j,0,0);
//            this.setTile(Tile.Tiles.tileGrass, -j,0,0);
//            this.setTile(Tile.Tiles.tileGrass, 0,j,0);
//            this.setTile(Tile.Tiles.tileGrass, 0,-j,0);
//        }
//        i++;

    }
}
