package com.deadvikingstudios.norsetown.view.meshes;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 6/20/2017.
 */
public class EntityMesh extends TexturedMesh
{
    Entity entity;

    protected EntityMesh(Entity entity, MeshTexture texture)
    {
        super(texture);
        this.entity = entity;
    }

    public EntityMesh(Entity entity, RawMesh rawMesh, MeshTexture texture)
    {
        super(rawMesh, texture);
        this.entity = entity;
    }

    public EntityMesh(Entity entity, RawMesh rawMesh, MeshTexture texture, float shineDamper, float reflectivity)
    {
        super(rawMesh, texture, shineDamper, reflectivity);
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
}
