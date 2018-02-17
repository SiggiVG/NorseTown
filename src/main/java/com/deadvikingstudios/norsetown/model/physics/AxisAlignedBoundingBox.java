package com.deadvikingstudios.norsetown.model.physics;

import org.lwjgl.util.vector.Vector3f;

public class AxisAlignedBoundingBox
{
    private Vector3f min;
    private Vector3f max;

    //TODO: check that max is greater than min

    public AxisAlignedBoundingBox(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        this.min = new Vector3f(x1,y1,z1);
        this.max = new Vector3f(x2,y2,z2);
    }

    public AxisAlignedBoundingBox(Vector3f least, Vector3f greatest)
    {
        this.min = new Vector3f(least);
        this.max = new Vector3f(greatest);
    }

    public boolean isPointInside(Vector3f point)
    {
        return  (point.x >=this.min.x && point.x <= this.max.x) &&
                (point.y >=this.min.y && point.y <= this.max.y) &&
                (point.z >=this.min.z && point.z <= this.max.z);
    }

    public boolean collide(AxisAlignedBoundingBox collideWith)
    {
        return  (this.min.x <= collideWith.max.x && this.max.x >= collideWith.min.x) &&
                (this.min.y <= collideWith.max.y && this.max.y >= collideWith.min.y) &&
                (this.min.z <= collideWith.max.z && this.max.z >= collideWith.min.z);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AxisAlignedBoundingBox)) return false;

        AxisAlignedBoundingBox that = (AxisAlignedBoundingBox) o;

        if (min != null ? !min.equals(that.min) : that.min != null) return false;
        return max != null ? max.equals(that.max) : that.max == null;
    }

    @Override
    public int hashCode()
    {
        int result = min != null ? min.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }

    public Vector3f getMin()
    {
        return new Vector3f(min);
    }

    public void setMin(float x, float y, float z)
    {
        this.min = new Vector3f(x,y,z);
    }

    public void setMin(Vector3f min)
    {
        this.min = new Vector3f(min);
    }

    public Vector3f getMax()
    {
        return new Vector3f(max);
    }

    public void setMax(float x, float y, float z)
    {
        this.max = new Vector3f(x,y,z);
    }

    public void setMax(Vector3f max)
    {
        this.max = new Vector3f(max);
    }

    public float getMinX()
    {
        return min.x;
    }

    public float getMinY()
    {
        return min.y;
    }

    public float getMinZ()
    {
        return min.z;
    }

    public float getMaxX()
    {
        return max.x;
    }

    public float getMaxY()
    {
        return max.y;
    }

    public float getMaxZ()
    {
        return max.z;
    }
}
