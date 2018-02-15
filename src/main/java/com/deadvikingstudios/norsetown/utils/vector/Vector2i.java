package com.deadvikingstudios.norsetown.utils.vector;

public class Vector2i
{
    public final int x, y;

    public Vector2i(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2i(Vector2i position)
    {
        this.x = position.x;
        this.y = position.y;
    }

    @Override
    public String toString()
    {
        return "{"  + x +
                "," + y +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Vector2i)) return false;

        Vector2i that = (Vector2i) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
