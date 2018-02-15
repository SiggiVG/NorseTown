package com.deadvikingstudios.norsetown.model.world.gen;

import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.structures.Chunk;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.utils.Logger;

import java.util.Random;

public class IslandGenerator
{
    private Structure islandStructure;

    private PerlinNoise perlin;

    private final long seed;
    private int dimX, dimY, dimZ;

    public final static int MIN_DIM_XZ = 32;
    public final static int MIN_DIM_Y = 16;
    public final static int MAX_DIM_XZ = 128;
    public final static int MAX_DIM_Y = 64;

    public IslandGenerator(Structure islandStructure)
    {
        this.islandStructure = islandStructure;
        seed = System.currentTimeMillis();
        perlin = new PerlinNoise(seed);
        gen();
    }

    public Structure getIslandStructure()
    {
        return islandStructure;
    }

    private void gen()
    {
        this.genDimensions();

        for (int i = (int) (-dimX*0.5); i < (int)dimX*0.5; i++)
        {
            for (int k = (int) (-dimZ*0.5); k < (int)dimZ*0.5; k++)
            {
                int h = dimY - (int) (distanceTo(i,0,k,0));// / dimY);
//                Logger.debug("height = " + h);
                for (int j = h; j > 0; j--)
                {
                    this.islandStructure.setTile(Tile.Tiles.tileStoneCliff,i,j,k);
                }
            }
        }
    }

    private double distanceTo(int x1, int z1, int x2, int z2)
    {
        int x = x1 - x2;
        int z = z1 - z2;
        return Math.sqrt((x*x)+(z*z));
    }

    private void genDimensions()
    {
        Random rand = new Random(this.seed);
        this.dimX = rand.nextInt(MAX_DIM_XZ - MIN_DIM_XZ) + MIN_DIM_XZ;
        this.dimZ = rand.nextInt(MAX_DIM_XZ - MIN_DIM_XZ) + MIN_DIM_XZ;
        this.dimY = rand.nextInt(MAX_DIM_Y - MIN_DIM_Y) + MIN_DIM_Y;
    }
}
