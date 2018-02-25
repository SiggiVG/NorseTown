package com.deadvikingstudios.norsetown.model.tileenitites;

import com.deadvikingstudios.norsetown.model.entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by SiggiVG on 7/14/2017.
 * Extra information within a Tile, that is self aware
 */
public abstract class TileEntity extends Entity
{
    public TileEntity(Vector3f position)
    {
        super(position);
    }

    public TileEntity(float x, float y, float z)
    {
        super(x, y, z);
    }

    public boolean save(){return true;}
    public boolean load(){return true;}

    @Override
    public abstract void update();
}
