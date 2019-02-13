package com.deadvikingstudios.bygul.model.entities.humanoids;

import com.deadvikingstudios.bygul.model.entities.EntityLiving;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 7/13/2017.
 */
public class EntityHumanoid extends EntityLiving
{
    private EntityLiving target = null;
    public Vector3f headPos, headRot;
    public Vector3f chestPos, chestRot;
    public Vector3f hipPos, hipRot;

    public EntityHumanoid(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ)
    {
        super("player","humanoid", posX, posY, posZ);
    }
}
