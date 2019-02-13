package com.deadvikingstudios.bygul.utils.vector;

import org.lwjgl.util.vector.Vector3f;

public class Vector3i
{
    public final int x, y, z;

    public Vector3i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3i(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i(Vector3i position)
    {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
    }

    public Vector3i(float x, float y, float z)
    {
        this((int)x,(int)y,(int)z);
    }

    public Vector3i(Vector3f position)
    {
        this.x = (int)position.x;
        this.y = (int)position.y;
        this.z = (int)position.z;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Vector3i)) return false;

        Vector3i that = (Vector3i) o;

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

    @Override
    public String toString()
    {
        return "{"
                + x +
                "," + y +
                "," + z +
                '}';
    }

    public Vector2i toVector2i()
    {
        return new Vector2i(this.x, this.z);
    }
}
