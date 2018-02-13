package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.utils.Position3i;

public class EntityStructure extends Entity
{
    private final boolean canChangePosition;
    private final boolean canRotate;

    private Position3i structureOffset = new Position3i(0,0,0);

    private Structure structure;

    public <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure)
    {
        this(structure, 0, 0, 0);
    }

    public <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure, double x, double y, double z)
    {
        this(structure, x, y, z, false, false);
    }

    @Override
    public void update()
    {
        this.structure.update();
        //this.position.y += 0.1;
    }

    public Position3i getStructureOffset()
    {
        return structureOffset;
    }

    public void setStructureOffset(Position3i structureOffset)
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
