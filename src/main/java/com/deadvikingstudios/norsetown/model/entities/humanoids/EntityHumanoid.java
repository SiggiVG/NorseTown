package com.deadvikingstudios.norsetown.model.entities.humanoids;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 7/13/2017.
 */
public class EntityHumanoid extends Entity
{
    public Vector3f headPos, headRot;
    public Vector3f chestPos, chestRot;
    public Vector3f hipPos, hipRot;

    public EntityHumanoid(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ)
    {
        super(posX, posY, posZ, rotationX, rotationY, rotationZ, 1);
    }
}
