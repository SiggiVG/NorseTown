package com.deadvikingstudios.norsetown.model.entities.humanoids;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import com.deadvikingstudios.norsetown.model.entities.EntityLiving;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.utils.Maths;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

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
