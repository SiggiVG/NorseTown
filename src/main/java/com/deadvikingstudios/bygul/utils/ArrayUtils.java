package com.deadvikingstudios.bygul.utils;

import java.lang.reflect.Array;
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

    @SuppressWarnings("unchecked")
    public static <T> T[] deepCopyOf(T[] array)
    {
        if (0 >= array.length) return array;

        return (T[]) deepCopyOf( array, Array.newInstance(array[0].getClass(), array.length),0);
    }

    private static Object deepCopyOf(Object array, Object copiedArray, int index) {

        if (index >= Array.getLength(array)) return copiedArray;

        Object element = Array.get(array, index);

        if (element.getClass().isArray())
        {
            Array.set(copiedArray, index, deepCopyOf(
                    element,
                    Array.newInstance(
                            element.getClass().getComponentType(),
                            Array.getLength(element)),
                    0));
        }
        else
        {
            Array.set(copiedArray, index, element);
        }

        return deepCopyOf(array, copiedArray, ++index);
    }

}
