package com.deadvikingstudios.norsetown.model.world.gen;

import com.deadvikingstudios.bygul.model.tiles.Tile;
import com.deadvikingstudios.bygul.model.world.World;
import com.deadvikingstudios.bygul.model.world.gen.Generator;
import com.deadvikingstudios.bygul.model.world.gen.PerlinNoise;
import com.deadvikingstudios.bygul.model.world.structures.Structure;
import com.deadvikingstudios.bygul.model.world.structures.StructureTree;
import com.deadvikingstudios.bygul.utils.vector.Vector3i;
import com.deadvikingstudios.norsetown.model.tiles.NorseTiles;

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


                this.setTile(NorseTiles.tileGrass,i,-1,k);
                this.setTile(NorseTiles.tileSoil,i,-2,k);
                this.setTile(NorseTiles.tileSoil,i,-3,k);
                this.setTile(NorseTiles.tileSoil,i,-4,k);
                this.setTile(NorseTiles.tileStoneCliff, i, -5, k);
                this.setTile(NorseTiles.tileStoneCliff, i, -6, k);
                if(World.getUpdateRandom().nextInt(100) == 0)
                {
                    new StructureTree(new Vector3i(i, 0, k), this.structure, NorseTiles.tileTrunkFir, NorseTiles.tileLeaves);
                }
            }
        }

//        for (int i = -1; i < 15; i++)
//        {
//            for (int k = -1; k < 2; k++)
//            {
//                this.setTile(Tile.NorseTiles.tilePlank, i, 0, k);
//                this.setTile(Tile.NorseTiles.tilePlank, i, 1, k);
//            }
//        }
//        for(int i = 0; i < 15; i++)
//        {
//            this.setTile(Tile.NorseTiles.tileAir, i, 0, 0);
//            this.setTile(Tile.NorseTiles.tileAir, i, 1, 0);
//        }
//
//        for (int i = -15; i < 15; i++)
//        {
//            for (int k = 5; k < 20; k++)
//            {
//                this.setTile(Tile.NorseTiles.tilePlank, i, 0, k);
//            }
//            for (int k = 15; k < 20; k++)
//            {
//                this.setTile(Tile.NorseTiles.tilePlank, i, 1, k);
//            }
//        }
        for (int i = -20; i < 20; i+=5)
        {
//            this.getStructure().addDockedStructure(new StructureTree(new Vector3i(World.getUpdateRandom().nextInt(64)-32,0,World.getUpdateRandom().nextInt(64)-32), this.structure));
//            this.getStructure().addDockedStructure(new StructureTree(new Vector3i(-5,0,i), this.structure));
//            new StructureTree(new Vector3i(-5,0,i), this.structure);
        }


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
