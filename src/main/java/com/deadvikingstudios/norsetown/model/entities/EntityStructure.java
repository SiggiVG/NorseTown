package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;

public class EntityStructure extends Entity
{
    private final boolean canChangePosition;
    private final boolean canRotate;

    private Vector3i structureOffset = new Vector3i(0,0,0);

    private Structure structure;

    public <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure)
    {
        this(structure, structure.getPosition().x, structure.getPosition().y, structure.getPosition().z);
    }

    private <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure, double x, double y, double z)
    {
        this(structure, x, y, z, false, false);
    }

    @Override
    public void update()
    {
        this.structure.update();
        //this.position.y += 0.1;
    }

    public Vector3i getStructureOffset()
    {
        return structureOffset;
    }

    public void setStructureOffset(Vector3i structureOffset)
    {
        this.structureOffset = structureOffset;
    }

    public <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure, double x, double y, double z, boolean canChangePosition, boolean canRotate)
    {
        super((float) x, (float)y, (float)z, 0, 0, 0, 1);
        this.structure = structure;
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
