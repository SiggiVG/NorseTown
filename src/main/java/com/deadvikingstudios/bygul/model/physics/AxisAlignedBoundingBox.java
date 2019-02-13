package com.deadvikingstudios.bygul.model.physics;

import com.sun.istack.internal.NotNull;
import org.lwjgl.util.vector.Vector3f;

public class AxisAlignedBoundingBox
{
    public final Vector3f min;
    public final Vector3f max;

    //TODO: check that max is greater than min

    public AxisAlignedBoundingBox(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        this.min = new Vector3f(x1,y1,z1);
        this.max = new Vector3f(x2,y2,z2);
    }

    public AxisAlignedBoundingBox(@NotNull final Vector3f least, @NotNull final Vector3f greatest)
    {
        this.min = new Vector3f(least);
        this.max = new Vector3f(greatest);
    }

    public AxisAlignedBoundingBox(@NotNull final AxisAlignedBoundingBox that)
    {
        //uses getters so that it assigns copies of the vectors
        this.min = that.getMin();
        this.max = that.getMax();
    }

    public boolean isPointInside(@NotNull final Vector3f point)
    {
        if(point == null) return false;
        return  (point.x >=this.min.x && point.x <= this.max.x) &&
                (point.y >=this.min.y && point.y <= this.max.y) &&
                (point.z >=this.min.z && point.z <= this.max.z);
    }

    public boolean collide(@NotNull final AxisAlignedBoundingBox collideWith)
    {
        if(collideWith == null) return false;
        return  (this.min.x <= collideWith.max.x && this.max.x >= collideWith.min.x) &&
                (this.min.y <= collideWith.max.y && this.max.y >= collideWith.min.y) &&
                (this.min.z <= collideWith.max.z && this.max.z >= collideWith.min.z);
    }

    public Vector3f getMin()
    {
        return new Vector3f(min);
    }

//    public void setMin(float x, float y, float z)
//    {
//        this.min = new Vector3f(x,y,z);
//    }
//
//    public void setMin(@NotNull final Vector3f min)
//    {
//        if(min == null) return;
//        this.min = new Vector3f(min);
//    }

    public Vector3f getMax()
    {
        return new Vector3f(max);
    }

//    public void setMax(float x, float y, float z)
//    {
//        this.max = new Vector3f(x,y,z);
//    }
//
//    public void setMax(@NotNull final Vector3f max)
//    {
//        if(max == null) return;
//        this.max = new Vector3f(max);
//    }

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

    public AxisAlignedBoundingBox offset(@NotNull final Vector3f vec)
    {
        if(vec == null) return this;
        return new AxisAlignedBoundingBox(this.min.x+vec.x, this.min.y+vec.y, this.min.z+vec.z,
                this.max.x+vec.x, this.max.y+vec.y, this.max.z+vec.z);
    }

    public static AxisAlignedBoundingBox getUnitBox()
    {
        return new AxisAlignedBoundingBox(0,0,0,1,1,1);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AxisAlignedBoundingBox)) return false;

        AxisAlignedBoundingBox that = (AxisAlignedBoundingBox) o;

        if (getMin() != null ? !getMin().equals(that.getMin()) : that.getMin() != null) return false;
        return getMax() != null ? getMax().equals(that.getMax()) : that.getMax() == null;
    }

    @Override
    public int hashCode()
    {
        int result = getMin() != null ? getMin().hashCode() : 0;
        result = 31 * result + (getMax() != null ? getMax().hashCode() : 0);
        return result;
    }

    @Override
    protected AxisAlignedBoundingBox clone()
    {
        return new AxisAlignedBoundingBox(this);
    }
}
