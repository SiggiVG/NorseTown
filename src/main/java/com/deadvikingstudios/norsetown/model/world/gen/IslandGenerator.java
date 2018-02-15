package com.deadvikingstudios.norsetown.model.world.gen;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.model.world.structures.StructureTree;
import com.deadvikingstudios.norsetown.utils.vector.Vector2i;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

import java.util.Random;

public class IslandGenerator extends Generator
{
    private PerlinNoise perlin;
    private Random rand;

    private int dimX, dimY, dimZ;

    private final static int MIN_DIM_XZ = 32;
    private final static int MIN_DIM_Y = 16;
    private final static int MAX_DIM_XZ = 128;
    private final static int MAX_DIM_Y = 64;

    public IslandGenerator(Structure islandStructure)
    {
        super(islandStructure, System.currentTimeMillis());
        rand = new Random(this.seed);
        perlin = new PerlinNoise(this.seed);
        gen();
    }

    @Override
    protected void gen()
    {
        this.genDimensions();

        Vector2i pos = new Vector2i(0,0);

        int radius = (int) (dimY*0.5f);

        Vector2i near = new Vector2i((int) (-dimX*0.5), (int) (-dimZ*0.5));
        Vector2i far = new Vector2i((int) (dimX*0.5), (int) (dimZ*0.5));

        for (int i = near.x; i < far.x; i++)
        {
            for (int j = 0; j < dimY; j++)
            {
                for (int k = near.y; k < far.y; k++)
                {
                    if(distanceTo(i,k,0,0) < radius) this.getStructure().setTile(Tile.Tiles.tileStoneCliff, i,j,k);
                }
            }
        }

//        //weird cross
//        for (int i = near.x; i < far.x; i++)
//        {
//            for (int k = near.y; k < far.y; k++)
//            {
//                //TODO: different values for each quadrant
//                int h = (int) Math.min(distanceTo(i,pos.x,k,pos.y), distanceTo(-k,pos.x,i,pos.y));
//
//                for (int j = h; j >= 0; j--)
//                {
//                    this.islandStructure.setTile(Tile.Tiles.tileStoneCliff,i,j,k);
//                }
//            }
//        }
//        //standing pole
//        for (int i = 0; i < dimY+10; i++)
//        {
//            this.islandStructure.setTile(Tile.Tiles.tileTrunkFir, 0, i,0);
//        }

        //change stone to dirt+grass
        for (int i = near.x; i < far.x; i++)
        {
            for (int k = near.y; k < far.y; k++)
            {
                for (int j = dimY; j >= 0; j--)
                {
                    if(this.getTile(i,j,k) == Tile.Tiles.tileStoneCliff)
                    {
                        this.setTile(Tile.Tiles.tileGrass,i,j,k);
                        if(!this.getTile(i,j-1,k).isAir())
                            this.setTile(Tile.Tiles.tileSoil,i,j-1,k);
                        if(!this.getTile(i,j-2,k).isAir())
                            this.setTile(Tile.Tiles.tileSoil,i,j-2,k);
                        if(!this.getTile(i,j-3,k).isAir())
                            this.setTile(Tile.Tiles.tileSoil,i,j-3,k);
                        if(rand.nextInt(60) == 0)
                        this.getStructure().addDockedStructure(new StructureTree(new Vector3i(i,j,k)));
                        break;
                    }
                }
            }
        }


    }

    private double distanceTo(int x1, int z1, int x2, int z2)
    {
        int x = x2 - x1;
        int z = z2 - z1;
        return Math.sqrt((x*x)+(z*z));
    }

    private void genDimensions()
    {
        Random rand = new Random(this.seed);
        this.dimX = rand.nextInt(MAX_DIM_XZ - MIN_DIM_XZ) + MIN_DIM_XZ;
        this.dimZ = dimX;//rand.nextInt(MAX_DIM_XZ - MIN_DIM_XZ) + MIN_DIM_XZ;
        this.dimY = rand.nextInt(MAX_DIM_Y - MIN_DIM_Y) + MIN_DIM_Y;
    }
}
