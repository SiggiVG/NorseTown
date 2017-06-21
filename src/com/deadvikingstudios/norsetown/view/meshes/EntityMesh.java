package com.deadvikingstudios.norsetown.view.meshes;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class EntityMesh extends TexturedMesh
{
    Entity entity;

    public EntityMesh(Entity entity, RawMesh model, MeshTexture texture)
    {
        super(model, texture);
        this.entity = entity;
    }

    //Getters and Setters
    public Vector3f getPosition()
    {
        return entity.getPosition();
    }

    public float getRotationX()
    {
        return entity.getRotationX();
    }

    public float getRotationY()
    {
        return entity.getRotationY();
    }

    public float getRotationZ()
    {
        return entity.getRotationZ();
    }

    public float getScale()
    {
        return entity.getScale();
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    //transformations... these actually shouldn't be in here, as View files should not be able to manipulate Model files
    /*
    public void translate(float dx, float dy, float dz)
    {
        this.entity.translate(dx,dy,dz);
    }

    public void translate(Vector3f dVec)
    {
        this.entity.translate(dVec);
    }

    public void rotate(float dx, float dy, float dz)
    {
        this.entity.rotate(dx,dy,dz);
    }

    public void rotate(Vector3f dVec)
    {
        this.entity.rotate(dVec);
    }*/
}
