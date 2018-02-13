package com.deadvikingstudios.norsetown.utils;

public class Position3D
{
    public final int x, y, z;

    public Position3D(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position3D(Position3D position)
    {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Position3D)) return false;

        Position3D that = (Position3D) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        return z == that.z;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
