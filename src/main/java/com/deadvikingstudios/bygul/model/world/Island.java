package com.deadvikingstudios.bygul.model.world;

import org.lwjgl.util.vector.Vector2f;

import java.util.Random;

/**
 * Created by SiggiVG on 6/21/2017.
 *
 * Algorithm
 * Set edges to ocean
 * Assign water/land to the corners by setting Corner.water based on Perlin Noise
 * Assign water/land to the polygons by setting Center.water if some fraction of the corners have water set.
 * flood fill water to ocean, any remaining ocean is lakes
 * determine coasts, center.coast = true for land with an ocean corner
 *      corner.ocean if surrounded by ocean centers
 *      corner.coast if it touches ocean and land
 *      reset corner.water for consistencies
 *      coasts are only on oceans, not lakes
 * calculate elevation based on distance to water, setting it per corner
 *      center.elevation is the average of the center and it's corners
 *      lakes dont count towards distance and should create valleys around them
 * redistribute elevations to match desired elevation distribution
 * for each noncoast land corner, find it's downslope
 * calculate rivers, pick random corners in mountains
 *      and follow their downslope, incrementing each of the river values as you flow down
 *      rivers will naturally deposit into lakes, stop them when they do
 *      ***move about the edge of a lake, adding up the river values that deposit into it
 *      ***find the lake's lowest corner, and path a river from it doen the slope
 *      riverWidth = sqrt(riverVolume)???
 *
 *
 *
 *
 */
public class Island
{
    public static final int ISLAND_SIZE = 128;

    //private float[][] centerElevation = new float[ISLAND_SIZE][ISLAND_SIZE];
    //private float[][] cornerElevation = new float[ISLAND_SIZE+1][ISLAND_SIZE+1];

    private Center[][] centers = new Center[ISLAND_SIZE][ISLAND_SIZE];
    private Corner[][] corners = new Corner[ISLAND_SIZE+1][ISLAND_SIZE+1];

    private int seed;
    private Random random;

    public Island()
    {
        this((int) (System.currentTimeMillis() % Integer.MAX_VALUE));
    }

    public Island(int seed)
    {
        this.seed = seed;
        this.random = new Random(seed);
    }

    private class Center
    {
        public boolean isWater;
        public boolean isOcean;
        public boolean isCoast;
        public String biome = "";
        public float elevation; //0.0-1.0
        public float moisture; //0.0-1.0
    }

    private class Corner
    {
        public boolean isWater;
        public boolean isOcean;
        public boolean isCoast;
        public float elevation; //0.0-1.0
        public float moisture; //0.0-1.0

        public int river; //0 if no river, or volume of water in river
        public Vector2f downslope;
        public Vector2f watershed;
        public int watershedSize;

    }
}
