package com.deadvikingstudios.norsetown.model.entities.humanoids;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityLiving;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 7/13/2017.
 */
public class EntityHumanoid extends EntityLiving
{
    public Vector3f headPos, headRot;
    public Vector3f chestPos, chestRot;
    public Vector3f hipPos, hipRot;

    public EntityHumanoid(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ)
    {
        super("human", posX, posY, posZ);
    }
}
