package com.deadvikingstudios.norsetown.model.world.structures;

import com.deadvikingstudios.norsetown.model.entities.Entity;

public class EntityStructure extends Entity
{
    private final boolean canChangePosition;
    private final boolean canRotate;

    private Structure structure;

    public EntityStructure(double x, double y, double z)
    {
        this(x, y, z, false, false);

    }

    public EntityStructure(double x, double y, double z, boolean canChangePosition, boolean canRotate)
    {
        super((float) x, (float)y, (float)z, 0, 0, 0, 1);
        this.canChangePosition = canChangePosition;
        this.canRotate = canRotate;
    }

    public boolean isCanChangePosition()
    {
        return canChangePosition;
    }

    public boolean isCanRotate()
    {
        return canRotate;
    }

    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure)
    {
        this.structure = structure;
    }
}
