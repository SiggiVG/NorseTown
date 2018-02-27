package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.norsetown.model.world.structures.Structure;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class EntityStructure extends Entity
{
    private final boolean canChangePosition;
    private final boolean canRotate;

    private Structure structure;

    public <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure)
    {
        this(structure, structure.getPosition().x, structure.getPosition().y, structure.getPosition().z);
    }

    public <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure, int x, int y, int z)
    {
        this(structure,(double)x,(double)y,(double)z);
    }

    private <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure, double x, double y, double z)
    {
        this(structure, x, y, z, false, false);
    }

    @Override
    public void update()
    {
        this.structure.update();
    }

    @Override
    public List<AxisAlignedBoundingBox> getAxisAlignedBoundingBox()
    {
        return this.structure.getRoughCollider(true);
    }

    @Override
    public Vector3f getPosition()
    {
        return new Vector3f(this.structure.getPosition().x, this.structure.getPosition().y, this.structure.getPosition().z);
    }

    @Override
    public void setPosition(Vector3f position)
    {
        this.setPosition((int)position.getX(), (int)position.getY(), (int)position.getZ());
    }

    public void setPosition(Vector3i position)
    {
        this.setPosition((int)position.x, (int)position.y, (int)position.z);
    }

    public <STRUCTURE extends Structure> EntityStructure(STRUCTURE structure, double x, double y, double z, boolean canChangePosition, boolean canRotate)
    {
        super((float) x, (float)y, (float)z, 0, 0, 0, 1);
        this.structure = structure;
        this.structure.setPosition(new Vector3i((int)x,(int)y,(int)z));
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
