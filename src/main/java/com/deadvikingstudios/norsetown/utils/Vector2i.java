package com.deadvikingstudios.norsetown.utils;

public class Vector2i
{
    public final int x, z;

    public Vector2i(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    public Vector2i(Vector2i position)
    {
        this.x = position.x;
        this.z = position.z;
    }

    @Override
    public String toString()
    {
        return "{"  + x +
                "," + z +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Vector2i)) return false;

        Vector2i that = (Vector2i) o;

        if (x != that.x) return false;
        return z == that.z;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + z;
        return result;
    }
}
