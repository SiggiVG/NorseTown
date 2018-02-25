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

    private final static int MIN_DIM_XZ = 64;
    private final static int MIN_DIM_Y = 16;
    private final static int MAX_DIM_XZ = 256;
    private final static int MAX_DIM_Y = 64;

    public IslandGenerator(Structure islandStructure)
    {
        super(islandStructure, System.currentTimeMillis());
        perlin = new PerlinNoise(this.seed);
        gen();
    }

    @Override
    protected void gen()
    {
        rand = new Random(this.seed);

        Vector3i near = new Vector3i(-32, -6, -32);
        Vector3i far = new Vector3i(31, 0, 31);

        //change stone to dirt+grass
        for (int i = near.x; i < far.x; i++)
        {
            for (int k = near.z; k < far.z; k++)
            {


                this.setTile(Tile.Tiles.tileGrass,i,-1,k);
                this.setTile(Tile.Tiles.tileSoil,i,-2,k);
                this.setTile(Tile.Tiles.tileSoil,i,-3,k);
                this.setTile(Tile.Tiles.tileSoil,i,-4,k);
                this.setTile(Tile.Tiles.tileStoneCliff, i, -5, k);
                this.setTile(Tile.Tiles.tileStoneCliff, i, -6, k);
//                if(rand.nextInt(60) == 0)
//                        this.getStructure().addDockedStructure(new StructureTree(new Vector3i(i,0,k), this.structure));
            }
        }

        for (int i = -1; i < 15; i++)
        {
            for (int k = -1; k < 2; k++)
            {
                this.setTile(Tile.Tiles.tilePlank, i, 0, k);
                this.setTile(Tile.Tiles.tilePlank, i, 1, k);
            }
        }
        for(int i = 0; i < 15; i++)
        {
            this.setTile(Tile.Tiles.tileAir, i, 0, 0);
            this.setTile(Tile.Tiles.tileAir, i, 1, 0);
        }

        for (int i = -15; i < 15; i++)
        {
            for (int k = 5; k < 20; k++)
            {
                this.setTile(Tile.Tiles.tilePlank, i, 0, k);
            }
            for (int k = 15; k < 20; k++)
            {
                this.setTile(Tile.Tiles.tilePlank, i, 1, k);
            }
        }

        this.getStructure().addDockedStructure(new StructureTree(new Vector3i(-10,0,-10), this.structure));
    }

//    private double distanceTo(int x1, int z1, int x2, int z2)
//    {
//        int x = x2 - x1;
//        int z = z2 - z1;
//        return Math.sqrt((x*x)+(z*z));
//    }

//    private void genDimensions()
//    {
//        Random rand = new Random(this.seed);
//        this.dimX = rand.nextInt(MAX_DIM_XZ - MIN_DIM_XZ) + MIN_DIM_XZ;
//        this.dimZ = dimX;//rand.nextInt(MAX_DIM_XZ - MIN_DIM_XZ) + MIN_DIM_XZ;
//        this.dimY = rand.nextInt(MAX_DIM_Y - MIN_DIM_Y) + MIN_DIM_Y;
//    }
}
