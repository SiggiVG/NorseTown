package com.deadvikingstudios.norsetown.model.entities;

import com.deadvikingstudios.norsetown.model.entities.ai.pathfinding.Pathfinder;
import com.deadvikingstudios.norsetown.model.entities.ai.pathfinding.Node;
import com.deadvikingstudios.norsetown.model.physics.AxisAlignedBoundingBox;
import com.deadvikingstudios.norsetown.model.tiles.Tile;
import com.deadvikingstudios.norsetown.model.world.World;
import com.deadvikingstudios.norsetown.utils.Logger;
import com.deadvikingstudios.norsetown.utils.Maths;
import com.deadvikingstudios.norsetown.utils.vector.Vector3i;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EntityLiving extends Entity
{
    private String name = "";

    public boolean isAlive = true;
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    public int height = 2;

    protected AxisAlignedBoundingBox hitBox;

    public EntityLiving(String name, float x, float y, float z)
    {
        super(x,y,z);
        hitBox = new AxisAlignedBoundingBox(0,0,0,1,this.height,1);
        this.name = name;
    }

    /**
     * The position that it is lerping from
     */
    private Vector3i travelFrom;
    /**
     * the destination that it is lerping to
     */
    protected Vector3i destination;
    /**
     * it's progress in it's current lerp
     */
    private float moved = 0;
    /**
     * The path it is traveling
     */
    protected List<Node> path;

    private static final float VELOCITY_ADJ = 0.1f;
    private static final float VELOCITY_DIAG = VELOCITY_ADJ / (float) Math.sqrt(2);

    @Override
    public void update()
    {
        if(currentHealth <= 0)
        {
            this.isAlive = false;
            Logger.debug(this.name + " has perished");
        }

        if(this.isAlive)
        {
            doMovement();
        }
        else
        {
            World.getCurrentWorld().removeEntity(this);
        }
    }

    @Override
    public List<AxisAlignedBoundingBox> getAxisAlignedBoundingBox()
    {
        List<AxisAlignedBoundingBox> list = new ArrayList<AxisAlignedBoundingBox>();
        list.add(hitBox.offset(this.position));
        return list;
    }

    int counter = 0;
    //TODO: do pathfinding on another Thread
    protected void doMovement()
    {
        if (path != null)
        {
            if (path.size() > 0)
            {
                counter++;
                Vector3i vec = path.get(path.size() - 1).position;
//                if(World.getCurrentWorld().currentIsland.getTile(vec.x, vec.y, vec.z) != Tile.Tiles.tileAir)
                if(counter % 10 == 0)
                {
                    recalcPath();
                    counter = 0;
                }
                //Changes rotation based on movement direction
                this.rotateToFace(new Vector3f(vec.x, vec.y, vec.z));
                //Changes movespeed to keep the lerp constant
                float velocity;
                if(this.rotation.y % 90 != 0)
                {
                    velocity = VELOCITY_DIAG;
                }
                else
                {
                    velocity = VELOCITY_ADJ;
                }

                this.position = Maths.lerp(travelFrom, vec, moved);
                moved += velocity;

                if (moved >= 1f)
                {
                    moved = 0;
                    this.travelFrom = vec;
                    path.remove(path.size() - 1);
                }
            }
            if(this.destination != null)
            {
                if (this.destination.equals(this.travelFrom))
                {
                    //World.getCurrentWorld().currentIsland.setTile(Tile.Tiles.tileSoil, destination.x, destination.y, destination.z);
                    this.path = null;
                    this.destination = null;
                }
            }
        } else
        {
            this.acquireTarget();
            if (this.destination != null)
            {
                if (World.getCurrentWorld().currentIsland.getTile(destination.x, destination.y - 1, destination.z) == Tile.Tiles.tileAir)
                    return;
                for (int i = 0; i <= this.height; i++)
                {
                    if (World.getCurrentWorld().currentIsland.getTile(destination.x, destination.y + i, destination.z) != Tile.Tiles.tileAir)
                        return;

                    this.travelFrom = new Vector3i((int) Math.round(this.position.x), (int) Math.round(this.position.y), (int) Math.round(this.position.z));
                    path = Pathfinder.findPathAStar(World.getCurrentWorld().currentIsland, this, this.travelFrom, this.destination, true);
                }
            }
        }
    }

    protected void recalcPath()
    {
        if(travelFrom != null)
        {
            this.position.x = this.travelFrom.x;
            this.position.y = this.travelFrom.y;
            this.position.z = this.travelFrom.z;
            path = Pathfinder.findPathAStar(World.getCurrentWorld().currentIsland, this, this.travelFrom, this.destination, true);
        }
    }

    protected void acquireTarget()
    {
        this.destination = new Vector3i(
                World.getUpdateRandom().nextInt(64) - 32,
                World.getUpdateRandom().nextInt(32),
                World.getUpdateRandom().nextInt(64) - 32);
    }

    protected void rotateToFace(Vector3f vec)
    {
        float x = this.position.x - vec.x;
        float z = this.position.z - vec.z;

        this.rotation.y = (float) Math.toDegrees(Math.atan2(x, z));
    }
}
