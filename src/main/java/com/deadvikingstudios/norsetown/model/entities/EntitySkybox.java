package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Created by SiggiVG on 6/20/2017.
 *
 * TODO: make abstract
 */
public class EntitySkybox extends Entity
{

    public EntitySkybox(float posX, float posY, float posZ, float rotationX, float rotationY, float rotationZ)
    {
        super(posX, posY, posZ, rotationX, rotationY, rotationZ);
    }
}
