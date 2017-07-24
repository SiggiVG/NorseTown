package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.controller.GameContainer;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.view.meshes.RawMesh;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * TODO: make abstract
 */
public class Entity
{
    protected Vector3f position = new Vector3f();
    protected Vector3f rotation = new Vector3f();
    protected float scale = 1;

    protected Entity(){}

    public Entity(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ, float scale)
    {
        this.position.x = posX;
        this.position.y = posY;
        this.position.z = posZ;
        this.rotation.x = rotationX;
        this.rotation.y = rotationY;
        this.rotation.z = rotationZ;
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

    public void update()
    {

    }


    public Vector3f getPosition()
    {
        return new Vector3f(position);
    }

    public void setPosition(Vector3f position)
    {
        this.setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(float x, float y, float z)
    {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getPosX()
    {
        return position.x;
    }

    public void setPosX(float posX)
    {
        this.position.x = posX;
    }

    public float getPosY()
    {
        return position.y;
    }

    public void setPosY(float posY)
    {
        this.position.y = posY;
    }

    public float getPosZ()
    {
        return position.z;
    }

    public void setPosZ(float posZ)
    {
        this.position.z = posZ;
    }

    public float getRotationX()
    {
        return rotation.x;
    }

    public void setRotationX(float rotationX)
    {
        this.rotation.x = rotationX;
    }

    public float getRotationY()
    {
        return rotation.y;
    }

    public void setRotationY(float rotationY)
    {
        this.rotation.y = rotationY;
    }

    public float getRotationZ()
    {
        return rotation.z;
    }

    public void setRotationZ(float rotationZ)
    {
        this.rotation.z = rotationZ;
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
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void translate(Vector3f dVec)
    {
        this.translate(dVec.x, dVec.y,dVec.z);
    }

    public void rotate(float dx, float dy, float dz)
    {
        this.rotation.x += dx;
        this.rotation.y += dy;
        this.rotation.z += dz;
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
