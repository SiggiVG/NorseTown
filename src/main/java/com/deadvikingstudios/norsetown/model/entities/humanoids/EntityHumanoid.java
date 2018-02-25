package com.deadvikingstudios.norsetown.model.entities.humanoids;

import com.deadvikingstudios.norsetown.controller.CameraController;
import com.deadvikingstudios.norsetown.controller.GameContainer;
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
        super("humanoid", posX, posY, posZ);
    }

    int counter = 0;
    @Override
    public void update()
    {
        if(target == null)
        {
            List<Entity> entities = World.getCurrentWorld().getEntities();
            EntityLiving closestEnt = null;
            float shortDist = 0;
            for (Entity ent : entities)
            {
                if(ent instanceof EntityLiving && ent != this)
                {
                    if(closestEnt == null)
                    {
                        closestEnt = (EntityLiving) ent;
                        shortDist = Maths.distanceSquared(this.position, ent.getPosition());
                    }
                    else
                    {
                        float entDist = Maths.distanceSquared(this.position, ent.getPosition());
                        if(shortDist > entDist)
                        {
                            closestEnt = (EntityLiving) ent;
                        }
                    }
                }
            }
            this.target = closestEnt;
        }
        else
        {
            Vector3i targetPos = new Vector3i(target.getPosition());
            if (new Vector3i(this.position).equals(targetPos))
            {
                World.getCurrentWorld().removeEntity(target);
                this.target = null;
            }

            counter++;
            if (counter % 10 == 0)
            {
                this.acquireTarget();

                this.recalcPath();
            }
        }

        super.update();
    }

    @Override
    protected void acquireTarget()
    {
        if(target != null)
        {
            Vector3f t = target.getPosition();
            this.destination = new Vector3i((int)Math.round(t.x), (int)Math.round(t.y), (int)Math.round(t.z));
        }
        else
        {
            destination = null;
        }
    }
}
