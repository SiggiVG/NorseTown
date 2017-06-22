package com.deadvikingstudios.norsetown.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiggiVG on 6/21/2017.
 */
public class ArrayUtils
{
    public static float[] floatFromFloat(List<Float> floats)
    {
        float[] nums = new float[floats.size()];
        for (int i = 0; i < nums.length; i++)
        {
            nums[i] = floats.get(i);
        }
        return nums;
    }

    public static int[] intFromInteger(List<Integer> ints)
    {
        int[] nums = new int[ints.size()];
        for (int i = 0; i < nums.length; i++)
        {
            nums[i] = ints.get(i);
        }
        return nums;
    }

    public static List<Float> floatToFloat(float[] floats)
    {
        List<Float> nums = new ArrayList<Float>();
        for (int i = 0; i < floats.length; i++)
        {
            nums.add(floats[i]);
        }
        return nums;
    }

    public static List<Integer> intToInteger(int[] ints)
    {
        List<Integer> nums = new ArrayList<Integer>();
        for (int i = 0; i < ints.length; i++)
        {
            nums.add(ints[i]);
        }
        return nums;
    }

}
