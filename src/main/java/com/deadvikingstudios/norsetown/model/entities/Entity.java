package com.deadvikingstudios.norsetown.model.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class Entity
{
    protected float posX, posY, posZ;
    protected float rotationX, rotationY, rotationZ;
    protected float scale = 1;

    private Entity(){}

    public Entity(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ, float scale)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
    }

    public Entity(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ)
    {
        this(posX, posY, posZ, rotationX, rotationY, rotationZ, 1);
    }

    public Entity(Vector3f position, float rotationX, float rotationY, float rotationZ, float scale)
    {
        this(position.x, position.y, position.z, rotationX, rotationY, rotationZ, scale);
    }

    public Entity(Vector3f position, float rotationX, float rotationY, float rotationZ)
    {
        this(position, rotationX, rotationY, rotationZ, 1);
    }

    public Entity(Vector3f position, Vector3f rotation, float scale)
    {
        this(position, rotation.x, rotation.y, rotation.z, scale);
    }

    public Entity(Vector3f position, Vector3f rotation)
    {
        this(position, rotation, 1);
    }

    public Entity(Vector3f position, float scale)
    {
        this(position.x, position.y, position.z, 0, 0, 0, scale);
    }

    public Entity(Vector3f position)
    {
        this(position.x, position.y, position.z, 0, 0, 0);
    }

    public Entity(float x, float y, float z, float scale)
    {
        this(x, y, z, 0, 0, 0, scale);
    }

    public Entity(float x, float y, float z)
    {
        this(x, y, z, 0, 0, 0);
    }

    public Vector3f getPosition()
    {
        return new Vector3f(posX, posY, posZ);
    }

    public void setPosition(Vector3f position)
    {
        this.setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(float x, float y, float z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public float getPosX()
    {
        return posX;
    }

    public void setPosX(float posX)
    {
        this.posX = posX;
    }

    public float getPosY()
    {
        return posY;
    }

    public void setPosY(float posY)
    {
        this.posY = posY;
    }

    public float getPosZ()
    {
        return posZ;
    }

    public void setPosZ(float posZ)
    {
        this.posZ = posZ;
    }

    public float getRotationX()
    {
        return rotationX;
    }

    public void setRotationX(float rotationX)
    {
        this.rotationX = rotationX;
    }

    public float getRotationY()
    {
        return rotationY;
    }

    public void setRotationY(float rotationY)
    {
        this.rotationY = rotationY;
    }

    public float getRotationZ()
    {
        return rotationZ;
    }

    public void setRotationZ(float rotationZ)
    {
        this.rotationZ = rotationZ;
    }

    public float getScale()
    {
        return scale;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public void translate(float dx, float dy, float dz)
    {
        this.posX += dx;
        this.posY += dy;
        this.posZ += dz;
    }

    public void translate(Vector3f dVec)
    {
        this.translate(dVec.x, dVec.y,dVec.z);
    }

    public void rotate(float dx, float dy, float dz)
    {
        this.rotationX += dx;
        this.rotationY += dy;
        this.rotationZ += dz;
    }

    public void rotate(Vector3f dVec)
    {
        this.rotate(dVec.x, dVec.y, dVec.z);
    }

    public void scale(float scale)
    {
        this.scale += scale;
    }
}
